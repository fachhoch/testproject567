package org.sairam.ns.service.dao;

import java.util.List;

import org.sairam.ns.service.ManagerFactory;
import org.sairam.ns.service.dto.NsSearchDTO;
import org.sairam.ns.service.model.FItem;
import org.sairam.ns.service.model.Ns;
import org.sairam.ns.service.model.NsDisTeam;
import org.sairam.ns.service.model.User;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Query;

public class DefaultNsManager extends ObjectifyGenericDAO<Ns> implements NsManager {

	@Override
	public User getLead(Ns ns) {
		return ManagerFactory.getUserManager().get(ns.getLeadKey());
	}

	@Override
	public List<User> getDisTeamMembers(Ns ns) {
		return Lists.newArrayList(Iterables.transform(
					ManagerFactory.getNsDisTeamManager().listByProperty("ns", ns.getKey())
					, new Function<NsDisTeam, User>() {
						@Override
						public User apply(NsDisTeam nsDisTeam) {
							return ManagerFactory.getUserManager().get(nsDisTeam.getUserKey());
						}
				}));
	}

	@Override
	public List<FItem> getFItems(Ns ns) {
		return ManagerFactory.getFItemManger().listByProperty("ns", ns.getKey());
	}
	
	@Override
	public List<Ns> findNs(NsSearchDTO nsSearchDTO) {
		return listByDoWithOby(new NsRetriever(nsSearchDTO));
	}
	
	@Override
	public int getNsCount(NsSearchDTO nsSearchDTO) {
		return countByDoWithOby(new NsRetriever(nsSearchDTO){
			@Override
			protected void adPagination(Query<Ns> query) {
			}
		});
	}
	
	public class NsRetriever  extends DoWithOby<NsSearchDTO>{
		public NsRetriever(NsSearchDTO  nsSearchDTO) {
			super(nsSearchDTO);
		}
		@Override
		protected void addFilters(Query<Ns> query) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
