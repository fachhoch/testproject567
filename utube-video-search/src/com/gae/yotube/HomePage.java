package com.gae.yotube;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.types.resources.StringResource;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.wicketstuff.jquery.JQueryBehavior;

import com.gae.yotube.service.model.PaginationDTO;
import com.gae.yotube.service.model.SearchDTO;
import com.gae.yotube.service.model.Video;
import com.gae.yotube.util.ServiceUtil;

public class HomePage extends BasePage {
	
	private static final String CONTENT_ID="contentId";
	
	private DataTableFragment  dataTableFragment;
	
	private SearchDTO  searchDTO;
	
	public HomePage() {
		//add(new JqueryYtubeBehaviour());
		searchDTO= new SearchDTO();
		searchDTO.setPaginationDTO(new PaginationDTO(0, 20));
		dataTableFragment=new DataTableFragment(CONTENT_ID);
		add(dataTableFragment);
		add(new JQueryBehavior());
		add(new AbstractBehavior() {
			@Override
			public void renderHead(IHeaderResponse response) {
				super.renderHead(response);
				response.renderJavascript(ServiceUtil.GROOVY_XML.getScript("highlight_table"), "highlight_table");
			}
		});
	}
	
	
	
	private class ActionFragment  extends  Fragment {
			public ActionFragment(String id,final Video  video) {
				super(id, "actionFragment", HomePage.this);
				add(new AjaxLink<Void>("link"){
					@Override
					public void onClick(AjaxRequestTarget target) {
						Component  newComponent=new EmbeddedYoutTubeFragment(CONTENT_ID, video);
						getPage().get(CONTENT_ID).replaceWith(newComponent);
						target.addComponent(newComponent);
					}
				});
			}
	}
	private class DataTableFragment extends  Fragment{
		public DataTableFragment(String id) {
			super(id, "dataTableFragment", HomePage.this);
			setOutputMarkupId(true);

			Form<SearchDTO>  form= new Form<SearchDTO>("searchForm", new CompoundPropertyModel<SearchDTO>(searchDTO));
			form.add(new TextField<String>("name"));
			form.add(new AjaxButton("submit") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					target.addComponent(DataTableFragment.this.get("datatable"));
				}
			});
			add(form);
			List<IColumn<Video>>  columns= new ArrayList<IColumn<Video>>();
			columns.add(new PropertyColumn<Video>(new Model<String>("Name"), "title"));
			columns.add(new AbstractColumn<Video>(new Model<String>("Link")) {
				@Override
				public void populateItem(Item<ICellPopulator<Video>> cellItem,String componentId, IModel<Video> model) {
					cellItem.add(new ActionFragment(componentId, model.getObject()));
				}
			});
			
			ISortableDataProvider<Video>  dataProvider= new SortableDataProvider<Video>() {

				@Override
				public Iterator<? extends Video> iterator(int first, int count) {
					PaginationDTO paginationDTO= searchDTO.getPaginationDTO();
					paginationDTO.setCount(count);
					paginationDTO.setFirst(first);
					searchDTO.setPaginationDTO(paginationDTO);
					return ServiceUtil.VIDEO_SERVICE.getVideos(searchDTO).iterator();
				}
				@Override
				public int size() {
					return ServiceUtil.VIDEO_SERVICE.getTotalVidoes(searchDTO);
				}

				@Override
				public IModel<Video> model(Video object) {
					return new Model<Video>(object);
				}
				
			};
			add(new AjaxFallbackDefaultDataTable<Video>("datatable",columns,dataProvider,20));
		}
	}
	
	private class EmbeddedYoutTubeFragment extends  Fragment {
		public EmbeddedYoutTubeFragment(String id, Video  video) {
			super(id, "youtubeFragment", HomePage.this);
			setOutputMarkupId(true);
			final String videoLink=StringUtils.replace("http://www.youtube.com/v/_PARAM_?version=3", "_PARAM_", getVideoId(video.getLink()));
			add(new WebMarkupContainer("name"){
				@Override
				protected void onComponentTag(ComponentTag tag) {
					tag.put("value", videoLink);
				}
			});
			add(new WebMarkupContainer("src"){
				@Override
				protected void onComponentTag(ComponentTag tag) {
					tag.put("src", videoLink);
				}
			});
			add(new AjaxLink<Void>("datatableLink"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					//Component  newComponent=new DataTableFragment(CONTENT_ID);
					getPage().get(CONTENT_ID).replaceWith(dataTableFragment);
					target.addComponent(dataTableFragment);
				}
			});
			
		}
	}
	private String getVideoId(String videoLink){
		int firstIndex=videoLink.indexOf("=");
		int lastIndex=videoLink.indexOf("&");
		return videoLink.substring(firstIndex+1, lastIndex);
	}
	
	private class SearchFragment extends  Fragment {
		public SearchFragment(String id) {
			super(id, "searchFragment", HomePage.this);
		}
	}
	
	
}
