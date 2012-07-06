/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-analyzer IKQueryParser.java 2012-7-6 10:23:22 l.xue.nong$$
 */
package org.wltea.analyzer.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

/**
 * The Class IKQueryParser.
 *
 * @author l.xue.nong
 */
public final class IKQueryParser {
	
	
	//查询关键字解析缓存线程本地变量
	/** The keyword cache thread local. */
	private static ThreadLocal<Map<String , TokenBranch>> keywordCacheThreadLocal 
			= new ThreadLocal<Map<String , TokenBranch>>();
	
	
	//是否采用最大词长分词
	/** The is max word length. */
	private static boolean isMaxWordLength = false;

	/**
	 * Sets the max word length.
	 *
	 * @param isMaxWordLength the new max word length
	 */
	public static void setMaxWordLength(boolean isMaxWordLength) {
		IKQueryParser.isMaxWordLength = isMaxWordLength ;
	}
	
	/**
	 * Optimize queries.
	 *
	 * @param queries the queries
	 * @return the query
	 */
	private static Query optimizeQueries(List<Query> queries){	
		//生成当前branch 的完整query
		if(queries.size() == 0){
			return null;
		}else if(queries.size() == 1){
			return queries.get(0);
		}else{
			BooleanQuery mustQueries = new BooleanQuery();
			for(Query q : queries){
				mustQueries.add(q, Occur.MUST);
			}
			return mustQueries;
		}			
	}
	
	/**
	 * Gets the thead local cache.
	 *
	 * @return the thead local cache
	 */
	private static Map<String , TokenBranch> getTheadLocalCache(){
		Map<String , TokenBranch> keywordCache = keywordCacheThreadLocal.get();
		if(keywordCache == null){
			 keywordCache = new HashMap<String , TokenBranch>(4);
			 keywordCacheThreadLocal.set(keywordCache);
		}
		return keywordCache;
	}
	
	/**
	 * Gets the cached token branch.
	 *
	 * @param query the query
	 * @return the cached token branch
	 */
	private static TokenBranch getCachedTokenBranch(String query){
		Map<String , TokenBranch> keywordCache = getTheadLocalCache();
		return keywordCache.get(query);
	}
	
	/**
	 * Cached token branch.
	 *
	 * @param query the query
	 * @param tb the tb
	 */
	private static void cachedTokenBranch(String query , TokenBranch tb){
		Map<String , TokenBranch> keywordCache = getTheadLocalCache();
		keywordCache.put(query, tb);
	}
		
	
	/**
	 * Parses the.
	 *
	 * @param field the field
	 * @param query the query
	 * @return the query
	 */
	public static Query parse(String field , String query){
		if(field == null){
			throw new IllegalArgumentException("parameter \"field\" is null");
		}

		if(query == null || "".equals(query.trim())){
			return new TermQuery(new Term(field));
		}
		
		//从缓存中取出已经解析的query生产的TokenBranch
		TokenBranch root = getCachedTokenBranch(query);
		if(root != null){
			return optimizeQueries(root.toQueries(field)); 
		}else{
			//System.out.println(System.currentTimeMillis());
			root = new TokenBranch(null);		
			//对查询条件q进行分词
			StringReader input = new StringReader(query.trim());
			IKSegmentation ikSeg = new IKSegmentation(input , isMaxWordLength);
			try {
				for(Lexeme lexeme = ikSeg.next() ; lexeme != null ; lexeme = ikSeg.next()){
					//处理词元分支
					root.accept(lexeme);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//缓存解析结果的博弈树
			cachedTokenBranch(query , root);
			return optimizeQueries(root.toQueries(field));
		}
	}
	
	/**
	 * Parses the.
	 *
	 * @param ikQueryExp the ik query exp
	 * @return the query
	 */
	public static Query parse(String ikQueryExp){
		ExpressionParser ikExpParser = new ExpressionParser();
		return ikExpParser.parserExp(ikQueryExp);
	}
	
	/**
	 * Parses the multi field.
	 *
	 * @param fields the fields
	 * @param query the query
	 * @return the query
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
//	public static Query parse(String field , String query){
//		if(field == null){
//			throw new IllegalArgumentException("parameter \"field\" is null");
//		}
//		String[] qParts = query.split("\\s");
//		if(qParts.length > 1){			
//			BooleanQuery resultQuery = new BooleanQuery();
//			for(String q : qParts){
//				//过滤掉由于连续空格造成的空字串
//				if("".equals(q)){
//					continue;
//				}
//				Query partQuery;
//				try {
//					partQuery = _parse(field , q);
//					if(partQuery != null && 
//					          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
//						resultQuery.add(partQuery, Occur.MUST); 
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			return resultQuery;
//		}else{
//			try {
//				return _parse(field , query);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
	
	/**
	 * 多Field,单条件查询分析
	 * @param fields -- Document fields name
	 * @param query	-- keyword
	 * @return Query 查询逻辑对象
	 * @throws IOException
	 */
	public static Query parseMultiField(String[] fields , String query) throws IOException{
		if(fields == null){
			throw new IllegalArgumentException("parameter \"fields\" is null");
		}		
		BooleanQuery resultQuery = new BooleanQuery();		
		for(String field : fields){
			if(field != null){
				Query partQuery = parse(field , query);
				if(partQuery != null && 
				          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
					resultQuery.add(partQuery, Occur.SHOULD); 
				}
			}			
		}		
		return resultQuery;
	}
	
	/**
	 * Parses the multi field.
	 *
	 * @param fields the fields
	 * @param query the query
	 * @param flags the flags
	 * @return the query
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Query parseMultiField(String[] fields , String query ,  BooleanClause.Occur[] flags) throws IOException{
		if(fields == null){
			throw new IllegalArgumentException("parameter \"fields\" is null");
		}
		if(flags == null){
			throw new IllegalArgumentException("parameter \"flags\" is null");
		}
		
		if (flags.length != fields.length){
		      throw new IllegalArgumentException("flags.length != fields.length");
		}		
		
		BooleanQuery resultQuery = new BooleanQuery();		
		for(int i = 0; i < fields.length; i++){
			if(fields[i] != null){
				Query partQuery = parse(fields[i] , query);
				if(partQuery != null && 
				          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
					resultQuery.add(partQuery, flags[i]); 
				}
			}			
		}		
		return resultQuery;
	}
	
	/**
	 * Parses the multi field.
	 *
	 * @param fields the fields
	 * @param queries the queries
	 * @return the query
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Query parseMultiField(String[] fields , String[] queries) throws IOException{
		if(fields == null){
			throw new IllegalArgumentException("parameter \"fields\" is null");
		}				
		if(queries == null){
			throw new IllegalArgumentException("parameter \"queries\" is null");
		}				
		if (queries.length != fields.length){
		      throw new IllegalArgumentException("queries.length != fields.length");
		}
		BooleanQuery resultQuery = new BooleanQuery();		
		for(int i = 0; i < fields.length; i++){
			if(fields[i] != null){
				Query partQuery = parse(fields[i] , queries[i]);
				if(partQuery != null && 
				          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
					resultQuery.add(partQuery, Occur.SHOULD); 
				}
			}			
		}		
		return resultQuery;
	}

	/**
	 * Parses the multi field.
	 *
	 * @param fields the fields
	 * @param queries the queries
	 * @param flags the flags
	 * @return the query
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Query parseMultiField(String[] fields , String[] queries , BooleanClause.Occur[] flags) throws IOException{
		if(fields == null){
			throw new IllegalArgumentException("parameter \"fields\" is null");
		}				
		if(queries == null){
			throw new IllegalArgumentException("parameter \"queries\" is null");
		}
		if(flags == null){
			throw new IllegalArgumentException("parameter \"flags\" is null");
		}
		
	    if (!(queries.length == fields.length && queries.length == flags.length)){
	        throw new IllegalArgumentException("queries, fields, and flags array have have different length");
	    }

	    BooleanQuery resultQuery = new BooleanQuery();		
		for(int i = 0; i < fields.length; i++){
			if(fields[i] != null){
				Query partQuery = parse(fields[i] , queries[i]);
				if(partQuery != null && 
				          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
					resultQuery.add(partQuery, flags[i]); 
				}
			}			
		}		
		return resultQuery;
	}	
	
	/**
	 * The Class TokenBranch.
	 *
	 * @author l.xue.nong
	 */
	private static class TokenBranch{
		
		/** The Constant REFUSED. */
		private static final int REFUSED = -1;
		
		/** The Constant ACCEPTED. */
		private static final int ACCEPTED = 0;
		
		/** The Constant TONEXT. */
		private static final int TONEXT = 1;
		
		//词元分支左边界
		/** The left border. */
		private int leftBorder;
		//词元分支右边界
		/** The right border. */
		private int rightBorder;
		//当前分支主词元
		/** The lexeme. */
		private Lexeme lexeme;
		//当前分支可并入的词元分支
		/** The accepted branchs. */
		private List<TokenBranch> acceptedBranchs;
		//当前分支的后一个相邻分支
		/** The next branch. */
		private TokenBranch nextBranch;
		
		/**
		 * Instantiates a new token branch.
		 *
		 * @param lexeme the lexeme
		 */
		TokenBranch(Lexeme lexeme){
			if(lexeme != null){
				this.lexeme = lexeme;
				//初始化branch的左右边界
				this.leftBorder = lexeme.getBeginPosition();
				this.rightBorder = lexeme.getEndPosition();
			}
		}
		
		/**
		 * Gets the left border.
		 *
		 * @return the left border
		 */
		public int getLeftBorder() {
			return leftBorder;
		}

		/**
		 * Gets the right border.
		 *
		 * @return the right border
		 */
		public int getRightBorder() {
			return rightBorder;
		}

		/**
		 * Gets the lexeme.
		 *
		 * @return the lexeme
		 */
		public Lexeme getLexeme() {
			return lexeme;
		}

		/**
		 * Gets the accepted branchs.
		 *
		 * @return the accepted branchs
		 */
		public List<TokenBranch> getAcceptedBranchs() {
			return acceptedBranchs;
		}

		/**
		 * Gets the next branch.
		 *
		 * @return the next branch
		 */
		public TokenBranch getNextBranch() {
			return nextBranch;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode(){
			if(this.lexeme == null){
				return 0;
			}else{
				return this.lexeme.hashCode() * 37;
			}
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o){			
			if(o == null){
				return false;
			}		
			if(this == o){
				return true;
			}
			if(o instanceof TokenBranch){
				TokenBranch other = (TokenBranch)o;
				if(this.lexeme == null ||
						other.getLexeme() == null){
					return false;
				}else{
					return this.lexeme.equals(other.getLexeme());
				}
			}else{
				return false;
			}			
		}	
		
		/**
		 * Accept.
		 *
		 * @param _lexeme the _lexeme
		 * @return true, if successful
		 */
		boolean accept(Lexeme _lexeme){
			
			/*
			 * 检查新的lexeme 对当前的branch 的可接受类型
			 * acceptType : REFUSED  不能接受
			 * acceptType : ACCEPTED 接受
			 * acceptType : TONEXT   由相邻分支接受 
			 */			
			int acceptType = checkAccept(_lexeme);			
			switch(acceptType){
			case REFUSED:
				// REFUSE 情况
				return false;
				
			case ACCEPTED : 
				if(acceptedBranchs == null){
					//当前branch没有子branch，则添加到当前branch下
					acceptedBranchs = new ArrayList<TokenBranch>(2);
					acceptedBranchs.add(new TokenBranch(_lexeme));					
				}else{
					boolean acceptedByChild = false;
					//当前branch拥有子branch，则优先由子branch接纳
					for(TokenBranch childBranch : acceptedBranchs){
						acceptedByChild = childBranch.accept(_lexeme) || acceptedByChild;
					}
					//如果所有的子branch不能接纳，则由当前branch接纳
					if(!acceptedByChild){
						acceptedBranchs.add(new TokenBranch(_lexeme));
					}					
				}
				//设置branch的最大右边界
				if(_lexeme.getEndPosition() > this.rightBorder){
					this.rightBorder = _lexeme.getEndPosition();
				}
				break;
				
			case TONEXT : 
				//把lexeme放入当前branch的相邻分支
				if(this.nextBranch == null){
					//如果还没有相邻分支，则建立一个不交叠的分支
					this.nextBranch = new TokenBranch(null);
				}
				this.nextBranch.accept(_lexeme);
				break;
			}

			return true;
		}
		
		/**
		 * To queries.
		 *
		 * @param fieldName the field name
		 * @return the list
		 */
		List<Query> toQueries(String fieldName){			
			List<Query> queries = new ArrayList<Query>(1);			
 			//生成当前branch 的query
			if(lexeme != null){
				queries.add(new TermQuery(new Term(fieldName , lexeme.getLexemeText())));
			}			
			//生成child branch 的query
			if(acceptedBranchs != null && acceptedBranchs.size() > 0){
				if(acceptedBranchs.size() == 1){
					Query onlyOneQuery = optimizeQueries(acceptedBranchs.get(0).toQueries(fieldName));
					if(onlyOneQuery != null){
						queries.add(onlyOneQuery);
					}					
				}else{
					BooleanQuery orQuery = new BooleanQuery();
					for(TokenBranch childBranch : acceptedBranchs){
						Query childQuery = optimizeQueries(childBranch.toQueries(fieldName));
						if(childQuery != null){
							orQuery.add(childQuery, Occur.SHOULD);
						}
					}
					if(orQuery.getClauses().length > 0){
						queries.add(orQuery);
					}
				}
			}			
			//生成nextBranch的query
			if(nextBranch != null){				
				queries.addAll(nextBranch.toQueries(fieldName));
			}
			return queries;	
		}
		
		/**
		 * Check accept.
		 *
		 * @param _lexeme the _lexeme
		 * @return the int
		 */
		private int checkAccept(Lexeme _lexeme){
			int acceptType = 0;
			
			if(_lexeme == null){
				throw new IllegalArgumentException("parameter:lexeme is null");
			}
			
			if(null == this.lexeme){//当前的branch是一个不交叠（ROOT）的分支
				if(this.rightBorder > 0  //说明当前branch内至少有一个lexeme
						&& _lexeme.getBeginPosition() >= this.rightBorder){
					//_lexeme 与 当前的branch不相交
					acceptType = TONEXT;
				}else{
					acceptType = ACCEPTED;
				}				
			}else{//当前的branch是一个有交叠的分支
				
				if(_lexeme.getBeginPosition() < this.lexeme.getBeginPosition()){
					//_lexeme 的位置比 this.lexeme还靠前（这种情况不应该发生）
					acceptType = REFUSED;
				}else if(_lexeme.getBeginPosition() >= this.lexeme.getBeginPosition()
							&& _lexeme.getBeginPosition() < this.lexeme.getEndPosition()){
					// _lexeme 与 this.lexeme相交
					acceptType = REFUSED;
				}else if(_lexeme.getBeginPosition() >= this.lexeme.getEndPosition()
							&& _lexeme.getBeginPosition() < this.rightBorder){
					//_lexeme 与 this.lexeme 不相交， 但_lexeme 与 当前的branch相交
					acceptType = ACCEPTED;
				}else{//_lexeme.getBeginPosition() >= this.rightBorder
					//_lexeme 与 当前的branch不相交
					acceptType=  TONEXT;
				}
			}
			return acceptType;
		}
	
	}
	
	/**
	 * The Class ExpressionParser.
	 *
	 * @author l.xue.nong
	 */
	static class ExpressionParser {
		
		//public static final String LUCENE_SPECIAL_CHAR = "&&||-()':={}[],";
		
		/** The elements. */
		private List<Element> elements = new ArrayList<Element>();
		
		/** The querys. */
		private Stack<Query> querys =  new Stack<Query>();
		
		/** The operates. */
		private Stack<Element> operates = new Stack<Element>();
		
		/**
		 * Instantiates a new expression parser.
		 */
		public ExpressionParser(){
		}

		/**
		 * Parser exp.
		 *
		 * @param expression the expression
		 * @return the query
		 */
		public Query parserExp(String expression){
			Query lucenceQuery = null;
			try{
				//文法解析
				this.splitElements(expression);
				//语法解析
				this.parseSyntax();
				if(this.querys.size() == 1){
					lucenceQuery = this.querys.pop();
				}else{
					throw new IllegalStateException("表达式异常： 缺少逻辑操作符 或 括号缺失");
				}
			}finally{
				elements.clear();
				querys.clear();
				operates.clear();
			}		
			return lucenceQuery;
		}
		
		/**
		 * Split elements.
		 *
		 * @param expression the expression
		 */
		private void splitElements(String expression){
	 		
			if(expression == null){
				return;
			}
			Element curretElement = null;
			
			char[] expChars = expression.toCharArray();
			for(int i = 0 ; i < expChars.length ; i++){
				switch(expChars[i]){
				case '&' :
					if(curretElement == null){
						curretElement = new Element();
						curretElement.type = '&';
						curretElement.append(expChars[i]);
					}else if(curretElement.type == '&'){
						curretElement.append(expChars[i]);
						this.elements.add(curretElement);
						curretElement = null;
					}else if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
					}else {
						this.elements.add(curretElement);
						curretElement = new Element();
						curretElement.type = '&';
						curretElement.append(expChars[i]);
					}
					break;
					
				case '|' :
					if(curretElement == null){
						curretElement = new Element();
						curretElement.type = '|';
						curretElement.append(expChars[i]);
					}else if(curretElement.type == '|'){
						curretElement.append(expChars[i]);
						this.elements.add(curretElement);
						curretElement = null;
					}else if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
					}else {
						this.elements.add(curretElement);
						curretElement = new Element();
						curretElement.type = '|';
						curretElement.append(expChars[i]);
					}				
					break;
					
				case '-' :
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = '-';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;			
					break;

				case '(' :
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = '(';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;			
					break;				

				case ')' :
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = ')';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;			
					break;					

				case ':' :
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = ':';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;			
					break;	
				
				case '=' :
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = '=';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;			
					break;					

				case ' ' :
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
						}else{
							this.elements.add(curretElement);
							curretElement = null;
						}
					}
					
					break;
				
				case '\'' :
					if(curretElement == null){
						curretElement = new Element();
						curretElement.type = '\'';
						
					}else if(curretElement.type == '\''){
						this.elements.add(curretElement);
						curretElement = null;
						
					}else{
						this.elements.add(curretElement);
						curretElement = new Element();
						curretElement.type = '\'';
						
					}
					break;
					
				case '[':
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = '[';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;					
					break;
					
				case ']':
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = ']';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;
					
					break;
					
				case '{':
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = '{';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;					
					break;
					
				case '}':
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = '}';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;
					
					break;
				case ',':
					if(curretElement != null){
						if(curretElement.type == '\''){
							curretElement.append(expChars[i]);
							continue;
						}else{
							this.elements.add(curretElement);
						}
					}
					curretElement = new Element();
					curretElement.type = ',';
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;
					
					break;
					
				default :
					if(curretElement == null){
						curretElement = new Element();
						curretElement.type = 'F';
						curretElement.append(expChars[i]);
						
					}else if(curretElement.type == 'F'){
						curretElement.append(expChars[i]);
						
					}else if(curretElement.type == '\''){
						curretElement.append(expChars[i]);

					}else{
						this.elements.add(curretElement);
						curretElement = new Element();
						curretElement.type = 'F';
						curretElement.append(expChars[i]);
					}			
				}
			}
			
			if(curretElement != null){
				this.elements.add(curretElement);
				curretElement = null;
			}
		}
		
		/**
		 * Parses the syntax.
		 */
		private void parseSyntax(){
			for(int i = 0 ; i < this.elements.size() ; i++){
				Element e = this.elements.get(i);
				if('F' == e.type){
					Element e2 = this.elements.get(i + 1);
					if('=' != e2.type && ':' != e2.type){
						throw new IllegalStateException("表达式异常： = 或 ： 号丢失");
					}
					Element e3 = this.elements.get(i + 2);
					//处理 = 和 ： 运算
					if('\'' == e3.type){
						i+=2;
						if('=' == e2.type){
							TermQuery tQuery = new TermQuery(new Term(e.toString() , e3.toString()));
							this.querys.push(tQuery);
						}else if(':' == e2.type){
							String keyword = e3.toString();
							if(keyword.startsWith("^") && keyword.endsWith("$")){
								Query pQuery = this.luceneQueryParse(e.toString(), keyword);
								this.querys.push(pQuery);
							}else{
								Query tQuery = IKQueryParser.parse(e.toString(), e3.toString());
								this.querys.push(tQuery);
							}
						}
						
					}else if('[' == e3.type || '{' == e3.type){
						i+=2;
						//处理 [] 和 {}
						LinkedList<Element> eQueue = new LinkedList<Element>();
						eQueue.add(e3);
						for( i++ ; i < this.elements.size() ; i++){							
							Element eN = this.elements.get(i);
							eQueue.add(eN);
							if(']' == eN.type || '}' == eN.type){
								break;
							}
						}
						//翻译RangeQuery
						Query rangeQuery = this.toTermRangeQuery(e , eQueue);
						this.querys.push(rangeQuery);
					}else{
						throw new IllegalStateException("表达式异常：匹配值丢失");
					}
					
				}else if('(' == e.type){
					this.operates.push(e);
					
				}else if(')' == e.type){
					boolean doPop = true;
					while(doPop && !this.operates.empty()){
						Element op = this.operates.pop();
						if('(' == op.type){
							doPop = false;
						}else {
							Query q = toQuery(op);
							this.querys.push(q);
						}
						
					}
				}else{ 
					
					if(this.operates.isEmpty()){
						this.operates.push(e);
					}else{
						boolean doPeek = true;
						while(doPeek && !this.operates.isEmpty()){
							Element eleOnTop = this.operates.peek();
							if('(' == eleOnTop.type){
								doPeek = false;
								this.operates.push(e);
							}else if(compare(e , eleOnTop) == 1){
								this.operates.push(e);
								doPeek = false;
							}else if(compare(e , eleOnTop) == 0){
								Query q = toQuery(eleOnTop);
								this.operates.pop();
								this.querys.push(q);
							}else{
								Query q = toQuery(eleOnTop);
								this.operates.pop();
								this.querys.push(q);
							}
						}
						
						if(doPeek && this.operates.empty()){
							this.operates.push(e);
						}
					}
				}			
			}
			
			while(!this.operates.isEmpty()){
				Element eleOnTop = this.operates.pop();
				Query q = toQuery(eleOnTop);
				this.querys.push(q);			
			}		
		}
		
		/**
		 * To query.
		 *
		 * @param op the op
		 * @return the query
		 */
		private Query toQuery(Element op){
			if(this.querys.size() == 0){
				return null;
			}
			
			BooleanQuery resultQuery = new BooleanQuery();

			if(this.querys.size() == 1){
				return this.querys.get(0);
			}
			
			Query q2 = this.querys.pop();
			Query q1 = this.querys.pop();
			if('&' == op.type){
				if(q1 != null){
					if(q1 instanceof BooleanQuery){
						BooleanClause[] clauses = ((BooleanQuery)q1).getClauses();
						if(clauses.length > 0 
								&& clauses[0].getOccur() == Occur.MUST){
							for(BooleanClause c : clauses){
								resultQuery.add(c);
							}					
						}else{
							resultQuery.add(q1,Occur.MUST);
						}

					}else{
						//q1 instanceof TermQuery 
						//q1 instanceof TermRangeQuery 
						//q1 instanceof PhraseQuery
						//others
						resultQuery.add(q1,Occur.MUST);
					}
				}
				
				if(q2 != null){
					if(q2 instanceof BooleanQuery){
						BooleanClause[] clauses = ((BooleanQuery)q2).getClauses();
						if(clauses.length > 0 
								&& clauses[0].getOccur() == Occur.MUST){
							for(BooleanClause c : clauses){
								resultQuery.add(c);
							}					
						}else{
							resultQuery.add(q2,Occur.MUST);
						}
						
					}else{
						//q1 instanceof TermQuery 
						//q1 instanceof TermRangeQuery 
						//q1 instanceof PhraseQuery
						//others
						resultQuery.add(q2,Occur.MUST);
					}
				}
				
			}else if('|' == op.type){
				if(q1 != null){
					if(q1 instanceof BooleanQuery){
						BooleanClause[] clauses = ((BooleanQuery)q1).getClauses();
						if(clauses.length > 0 
								&& clauses[0].getOccur() == Occur.SHOULD){
							for(BooleanClause c : clauses){
								resultQuery.add(c);
							}					
						}else{
							resultQuery.add(q1,Occur.SHOULD);
						}
						
					}else{
						//q1 instanceof TermQuery 
						//q1 instanceof TermRangeQuery 
						//q1 instanceof PhraseQuery
						//others
						resultQuery.add(q1,Occur.SHOULD);
					}
				}
				
				if(q2 != null){
					if(q2 instanceof BooleanQuery){
						BooleanClause[] clauses = ((BooleanQuery)q2).getClauses();
						if(clauses.length > 0 
								&& clauses[0].getOccur() == Occur.SHOULD){
							for(BooleanClause c : clauses){
								resultQuery.add(c);
							}					
						}else{
							resultQuery.add(q2,Occur.SHOULD);
						}
					}else{
						//q2 instanceof TermQuery 
						//q2 instanceof TermRangeQuery 
						//q2 instanceof PhraseQuery
						//others
						resultQuery.add(q2,Occur.SHOULD);
						
					}
				}
				
			}else if('-' == op.type){
				if(q1 == null || q2 == null){
					throw new IllegalStateException("表达式异常：SubQuery 个数不匹配");
				}
				
				if(q1 instanceof BooleanQuery){
					BooleanClause[] clauses = ((BooleanQuery)q1).getClauses();
					if(clauses.length > 0){
						for(BooleanClause c : clauses){
							resultQuery.add(c);
						}					
					}else{
						resultQuery.add(q1,Occur.MUST);
					}

				}else{
					//q1 instanceof TermQuery 
					//q1 instanceof TermRangeQuery 
					//q1 instanceof PhraseQuery
					//others
					resultQuery.add(q1,Occur.MUST);
				}				
				
				resultQuery.add(q2,Occur.MUST_NOT);
			}
			return resultQuery;
		}
		
		/**
		 * To term range query.
		 *
		 * @param fieldNameEle the field name ele
		 * @param elements the elements
		 * @return the term range query
		 */
		private TermRangeQuery toTermRangeQuery(Element fieldNameEle , LinkedList<Element> elements){

			boolean includeFirst = false;
			boolean includeLast = false;
			String firstValue = null;
			String lastValue = null;
			//检查第一个元素是否是[或者{
			Element first = elements.getFirst();
			if('[' == first.type){
				includeFirst = true;
			}else if('{' == first.type){
				includeFirst = false;
			}else {
				throw new IllegalStateException("表达式异常");
			}
			//检查最后一个元素是否是]或者}
			Element last = elements.getLast();
			if(']' == last.type){
				includeLast = true;
			}else if('}' == last.type){
				includeLast = false;
			}else {
				throw new IllegalStateException("表达式异常, RangeQuery缺少结束括号");
			}
			if(elements.size() < 4 || elements.size() > 5){
				throw new IllegalStateException("表达式异常, RangeQuery 错误");
			}			
			//读出中间部分
			Element e2 = elements.get(1);
			if('\'' == e2.type){
				firstValue = e2.toString();
				//
				Element e3 = elements.get(2);
				if(',' != e3.type){
					throw new IllegalStateException("表达式异常, RangeQuery缺少逗号分隔");
				}
				//
				Element e4 = elements.get(3);
				if('\'' == e4.type){
					lastValue = e4.toString();
				}else if(e4 != last){
					throw new IllegalStateException("表达式异常，RangeQuery格式错误");
				}				
			}else if(',' == e2.type){
				firstValue = null;
				//
				Element e3 = elements.get(2);
				if('\'' == e3.type){
					lastValue = e3.toString();
				}else{
					throw new IllegalStateException("表达式异常，RangeQuery格式错误");
				}
				
			}else {
				throw new IllegalStateException("表达式异常, RangeQuery格式错误");
			}
			
			return new TermRangeQuery(fieldNameEle.toString() , firstValue , lastValue , includeFirst , includeLast);
		}
		
		/**
		 * Lucene query parse.
		 *
		 * @param fieldName the field name
		 * @param keyword the keyword
		 * @return the query
		 */
		private Query luceneQueryParse(String fieldName , String keyword){
			//截取头部^尾部$
			keyword = keyword.substring(1 , keyword.length() - 1);
			String luceneExp = fieldName + ":\"" + keyword + "\"";
			QueryParser luceneQueryParser = new QueryParser(Version.LUCENE_30 , "" ,new IKAnalyzer());
			try {
				Query lucenceQuery = luceneQueryParser.parse(luceneExp);
				return lucenceQuery;
			} catch (ParseException e) {
				e.printStackTrace();
			}								
			return null;			
		}
		
		
		/**
		 * Compare.
		 *
		 * @param e1 the e1
		 * @param e2 the e2
		 * @return the int
		 */
		private int compare(Element e1 , Element e2){
			if('&' == e1.type){
				if('&' == e2.type){
					return 0;
				}else {
					return 1;
				}
			}else if('|' == e1.type){
				if('&' == e2.type){
					return -1;
				}else if('|' == e2.type){
					return 0;
				}else{
					return 1;
				}
			}else{
				if('-' == e2.type){
					return 0;
				}else{
					return -1;
				}
			}
		}
		
		/**
		 * The Class Element.
		 *
		 * @author l.xue.nong
		 */
		private class Element{
			
			/** The type. */
			char type = 0;
			
			/** The ele text buff. */
			StringBuffer eleTextBuff;

			/**
			 * Instantiates a new element.
			 */
			public Element(){
				eleTextBuff = new StringBuffer();
			}
			
			/**
			 * Append.
			 *
			 * @param c the c
			 */
			public void append(char c){
				this.eleTextBuff.append(c);
			}
		
			/* (non-Javadoc)
			 * @see java.lang.Object#toString()
			 */
			public String toString(){
				return this.eleTextBuff.toString();
			}
			
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		String ikQueryExp = "(id='ABcdRf' && date:{'20010101','20110101'} && keyword:'^魔兽中国$') || (content:'魔兽 中国'  || ulr='www.ik.com') - name:'林良益'";
//		String ikQueryExp = "content:'----'  || title:'----' - name:'林良益'";
		Query result = IKQueryParser.parse(ikQueryExp);
//		Query result = IKQueryParser.parse("(newsKeyword='---' || newsTitle:'---' || newsContent:'---') && newsClass='1'");
		System.out.println(result);

	}	
}
