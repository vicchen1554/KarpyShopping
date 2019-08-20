package com.web.store.service.impl;


import java.util.List;

import com.web.store.dao.ProductDao;
import com.web.store.dao.impl.ProductDaoImpl;
import com.web.store.model.ProductBean;
import com.web.store.service.ProductService;

public class ProductServiceImpl implements ProductService {
	
	ProductDao dao = null;
	
	public ProductServiceImpl() {
		dao = new ProductDaoImpl();
	}	
	
	public ProductServiceImpl(String dbString) {
		dao = new ProductDaoImpl(dbString);
	}	
	
	public ProductServiceImpl(String url, String account, String password) {
		dao = new ProductDaoImpl(url, account, password);
	}
	
	@Override
	public ProductBean select(Integer pId) {
		return dao.select(pId);
	}
	
	@Override
	public ProductBean select(String pname) {
		return dao.select(pname);
	}
	
	@Override
	public List<ProductBean> select() {
		return dao.select();
	}

	@Override
	public ProductBean insertProduct(ProductBean bean){
		return dao.insertProduct(bean);
	}
	
	@Override
	public long getRecordCounts() {
		return dao.getRecordCounts();
	}

	@Override
	public Integer delete(Integer pId) {
		return dao.delete(pId);
	}
	
	@Override
	public void close() {
		dao.close();
	}
}