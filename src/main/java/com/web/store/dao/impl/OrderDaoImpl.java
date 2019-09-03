﻿package com.web.store.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.web.store.dao.OrderDao;
import com.web.store.exception.OrderModificationException;
import com.web.store.exception.OrderNotFoundException;
import com.web.store.model.MemberBean;
import com.web.store.model.OrderBean;
import com.web.store.model.OrderItemBean;

@Repository
public class OrderDaoImpl implements OrderDao {

	@Autowired
	SessionFactory factory;
	

	@Override
	public OrderBean select(Integer oId) {
		Session session = factory.getCurrentSession();
		OrderBean order = session.get(OrderBean.class, oId); // TODO--add OrderNotFoundException handler
		if (order == null)
			throw new OrderNotFoundException("查無此訂單編號:" + oId );
		return order;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderBean> select() {
		String hql = "FROM OrderBean";
		List<OrderBean> orders = new ArrayList<OrderBean>();
		Session session = factory.getCurrentSession();
		orders = session.createQuery(hql).getResultList();
		if (orders.size() == 0)
			throw new OrderNotFoundException("目前無訂單紀錄");// TODO--add OrderNotFoundException handler
		return orders;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OrderBean> selectMemberOrders(Integer mId) {
		String hql = "FROM OrderBean where mId = :mId";
		List<OrderBean> orders = new ArrayList<OrderBean>();
		Session session = factory.getCurrentSession();
		orders = session.createQuery(hql).setParameter("mId",mId).getResultList();
		if (orders.size() == 0)
			throw new OrderNotFoundException("目前無訂單紀錄");// TODO--add OrderNotFoundException handler
		return orders;
	}
	

	@Override
	public OrderBean insertOrder(OrderBean bean) {
		OrderBean result = null;
		Session session = factory.getCurrentSession();
		MemberBean member = session.get(MemberBean.class, bean.getmId());
		bean.setMemberBean(member);
		session.save(bean);
		return result;
	}
	
//	//TODO--刪除錯誤程式碼
//	@Override
//	public OrderBean insertOrder(OrderBean bean,Set<OrderItemBean> items) {
//		OrderBean result = null;
//		Session session = factory.getCurrentSession();
//		MemberBean member = session.get(MemberBean.class, bean.getmId());
//		System.out.println("items=======" + items);
//		bean.setMemberBean(member);
////		session.save(items);
//		return result;
//	}

	@Override
	public Integer delete(Integer oId) {
		int count = 0;
		Session session = factory.getCurrentSession();
		OrderBean ob = session.get(OrderBean.class, oId);
		if (ob.getStatus().equals("未出貨")) {
			ob.setMemberBean(null);
			session.delete(ob);
			count++;
		}else {
			throw new OrderModificationException("訂單編號: " + oId + " 已出貨，無法修改訂單");// TODO--add cancelOrderException handler
		}
		return count;
	}

	// 依訂單編號查詢訂單細項
	@SuppressWarnings("unchecked")
	@Override
	public List<OrderItemBean> queryItems(Integer oId) {
		String hql = "From OrderItemBean where orderBean in (select oId from OrderBean where oId = :oId)";
		Session session = factory.getCurrentSession();
		List<OrderItemBean> oib = session.createQuery(hql).setParameter("oId", oId).getResultList();
		return oib;
	}

	@Override
	public Integer updateOrder(OrderBean ob) {
		Session session = factory.getCurrentSession();
		int count = 0;
		MemberBean mb = session.get(MemberBean.class, ob.getmId());// 也要加在add方法
		ob.setMemberBean(mb);// 也要加在add方法
		if (ob.getStatus().equals("未出貨")) {
//			ob.setConsignee(ob.getConsignee());
//			ob.setTel(ob.getAddr());
//			ob.setMemberBean(ob.getMemberBean());
			session.saveOrUpdate(ob);
			count++;
		} else {
			throw new OrderModificationException("訂單編號: " + ob.getoId() + " 已出貨，無法修改訂單");// TODO--add update exception handler
		}
		return count;
	}

	@Override
	public Integer VendorUpdateOrder(OrderBean ob) {
		Session session = factory.getCurrentSession();
		int count = 0;
		MemberBean mb = session.get(MemberBean.class, ob.getmId());// 也要加在add方法
		ob.setMemberBean(mb);// 也要加在add方法

		session.saveOrUpdate(ob);
		count++;

		return count;
	}
	

}
