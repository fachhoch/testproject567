package org.seva.dc.ns.web;

import java.util.Iterator;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.filters.ReplaceTokens;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.seva.dc.ns.domain.FItem;
import org.seva.dc.ns.domain.FItem.FType;
import org.seva.dc.ns.domain.Ns;
import org.seva.dc.ns.domain.User;
import org.seva.dc.ns.dto.NsSearchDTO;
import org.seva.dc.ns.dto.UserSearchDTO;
import org.seva.dc.ns.service.FItemService;
import org.seva.dc.ns.service.NSService;
import org.seva.dc.ns.service.UserService;
import org.seva.dc.ns.util.NSUtil;
import org.wicketstuff.jquery.datepicker.DatePickerBehavior;

import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.base.Predicate;
import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

public class HomePage extends BasePage {
		
	
	private static final String CONTENT_ID="contentId";
	
	
	private static final long serialVersionUID = 1992026987559149688L;

	private NsSearchDTO  nsSearchDTO= new NsSearchDTO();
	private UserSearchDTO  userSearchDTO= new UserSearchDTO();

	@SpringBean(name="nsService")
	private NSService  nsService;
	
	@SpringBean(name="userService")
	private UserService  userService;
	
	@SpringBean(name="fItemService")
	private FItemService  fItemService;
	
	private ModalWindow  modalWindow;
	
	public HomePage() {
		add(new UsersListsFragment(CONTENT_ID));
		modalWindow=new ModalWindow("modalWindow"){
			{
				setInitialHeight(300);
				setInitialWidth(400);
			}
		};
		add(modalWindow);
	}
	
	private  class NsListsFragment extends  Fragment{
		public NsListsFragment(String id) {
			super(id, "nsListsFragment", HomePage.this);
			setOutputMarkupId(true);
			List<IColumn<Ns>>   columns= Lists.newArrayList();
			columns.add(new PropertyColumn<Ns>(new Model<String>("Name"), "name"));
			columns.add(new PropertyColumn<Ns>(new Model<String>("Service On"), "nsOn"){
				@Override
				protected IModel<?> createLabelModel(IModel<Ns> rowModel) {
					return new Model<String>(NSUtil.formatDate(rowModel.getObject().getNsOn()));
				}
			});
			columns.add(new AbstractColumn<Ns>(new Model<String>("Action")) {
				@Override
				public void populateItem(Item<ICellPopulator<Ns>> cellItem,
						String componentId, IModel<Ns> rowModel) {
					cellItem.add(new EditNsLinkFragment(componentId, rowModel.getObject()));
				}
			});
			SortableDataProvider<Ns>  dataProvider= new SortableDataProvider<Ns>() {
				@Override
				public Iterator<? extends Ns> iterator(int first, int count) {
					nsSearchDTO.getPaginationDTO().setFirst(first);
					nsSearchDTO.getPaginationDTO().setCount(count);
					return nsService.findNs(nsSearchDTO).iterator();
				}
				@Override
				public IModel<Ns> model(Ns ns) {
					return new Model<Ns>(ns);
				}
				@Override
				public int size() {
					return nsService.getNsCount(nsSearchDTO);
				}
			};
			add(new AjaxFallbackDefaultDataTable<Ns>("datatable",columns,dataProvider, NSUtil.RECORDS_PER_PAGE));
			add(new AjaxLink<Void>("newNs"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					EditNsPropertiesFragment  editNsPropertiesFragment= new EditNsPropertiesFragment(CONTENT_ID, new Ns());
					NsListsFragment.this.replaceWith(editNsPropertiesFragment);
					target.addComponent(editNsPropertiesFragment);
				}
			});
			
			add(new AjaxLink<Void>("usersList"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					UsersListsFragment  usersListsFragment= new UsersListsFragment(CONTENT_ID);
					NsListsFragment.this.replaceWith(usersListsFragment);
					target.addComponent(usersListsFragment);
				}
			});
		}
	}

	private class EditNsLinkFragment  extends  Fragment{
		public EditNsLinkFragment(String id, final Ns  ns) {
			super(id, "editNsLinkFragment", HomePage.this);
			setOutputMarkupId(true);
			add(new AjaxLink<Void>("link") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					EditNsFragment  editNsFragment= new EditNsFragment(CONTENT_ID, ns);
					getPage().get(CONTENT_ID).replaceWith(editNsFragment);
					target.addComponent(editNsFragment);
				}
			});
		}
	}
	
	private class   EditNsFragment extends Fragment{
		public EditNsFragment(String id, Ns ns) {
			super(id, "editNsFragment", HomePage.this);
			setOutputMarkupId(true);
			ns=nsService.getById(ns.getId());
			add(new NsPropertiesFragment("nsPropertiesFragment", ns));
			add(new DisTeamFragment("disTeamFragment", ns));
			add(new FItemFragment("fItemFragment", ns));
			add(new AjaxLink<Void>("return"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					NsListsFragment nsListsFragment= new NsListsFragment(CONTENT_ID);
					EditNsFragment.this.replaceWith(nsListsFragment);
					target.addComponent(nsListsFragment);
				}
			});
		}
	}
	
	private class NsPropertiesFragment  extends  Fragment {
		public NsPropertiesFragment(String id, Ns  ns) {
			super(id, "nsPropertiesFragment", HomePage.this);
			setOutputMarkupId(true);
			add(new AjaxEditableLabel<String>("name", new Model<String>(ns.getName())));
			add(new AjaxEditableLabel<String>("nsOn", new Model<String>(NSUtil.formatDate(ns.getNsOn()))));
		}
	}
	
	
	private class EditNsPropertiesFragment  extends  Fragment {
		public EditNsPropertiesFragment(String id, Ns  ns) {
			super(id, "editNsPropertiesFragment", HomePage.this);
			setOutputMarkupId(true);
			Form<Ns>  form= new Form<Ns>("from", new CompoundPropertyModel<Ns>(ns));
			add(form);
			form.add(new FeedbackPanel("feedback"));
			form.add(new TextField<String>("name"));
			form.add(new DateTextField("nsOn").add(new DatePicker()));
			form.add(new AjaxButton("save",form) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					Ns  ns=(Ns)form.getModelObject();
					ns.setOwner(NSUtil.getDefaultUser());
					ns.setStatus(Ns.Status.OPEN);
					nsService.createOrUpdate(ns);
					replaceThis(target);
				}
			});
			form.add(new AjaxButton("cancel"){
				{
					setDefaultFormProcessing(false);
				}
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					replaceThis(target);
				}
			});
			form.add(new AbstractFormValidator() {
				@Override
				public void validate(Form<?> form) {
					FormComponent formComponent= (FormComponent)form.get("name");
					if(StringUtils.isBlank(formComponent.getValue())){
						formComponent.error("Name should not be blank");
					}
				}
				@Override
				public FormComponent<?>[] getDependentFormComponents() {
					return null;
				}
			});
		}
		protected void replaceThis(AjaxRequestTarget  target){
			NsListsFragment nsListsFragment= new NsListsFragment(CONTENT_ID);
			EditNsPropertiesFragment.this.replaceWith(nsListsFragment);
			target.addComponent(nsListsFragment);
		}
		
	}
	
	
	
	private class DisTeamFragment extends  Fragment{
		public DisTeamFragment(String id, Ns  ns) {
			super(id, "disTeamFragment", HomePage.this);
			setOutputMarkupId(true);
			add(new ListView<User>("disTeamMembersList", ns.getDisTeamMembers()){
				@Override
				protected void populateItem(ListItem<User> item) {
					add(new Label("name", NSUtil.formatUser(item.getModelObject())));
				}
			});
		}
	}
	
	private class FItemFragment extends  Fragment {
		Ns  ns;
		@SuppressWarnings("serial")
		public FItemFragment(String id,final  Ns  ns) {
			super(id, "fItemFragment", HomePage.this);
			this.ns=ns;
			setOutputMarkupId(true);
			final List<FItem>  fItems=ns.getfItems();
			add(new ListView<FType>("fItemsListView", Lists.newArrayList(FType.values())){
				@Override
				protected void populateItem(final ListItem<FType> item) {
					item.add(new Label("fItemName", item.getModelObject().toString()));
					item.add(new ListView<FItem>("fItemListView",Lists.newArrayList(Iterables.filter(fItems, new Predicate<FItem>() {
							@Override
							public boolean apply(FItem fItem) {
								return fItem.getFtType().equals(item.getModelObject());
							}
						}))) 
					{
						@Override
						protected void populateItem(ListItem<FItem> item) {
							final FItem  fItem=item.getModelObject();
							item.add(new Label("user", NSUtil.formatUser(fItem.getUser())));
							item.add(new Label("quantity",String.valueOf(fItem.getQuantity())));
							item.add(new AjaxLink<Void>("edit"){
								@Override
								public void onClick(AjaxRequestTarget target) {
									AddFitemFragment  addFitemFragment= new AddFitemFragment(modalWindow.getContentId(), ns){
										@Override
										protected void doAfterSave(AjaxRequestTarget target) {
											super.doAfterSave(target);
											modalWindow.close(target);
											replaceThis(target);
										}
										@Override
										protected FItem getFItem() {
											return fItemService.getById(fItem.getId());
										}
									};
									modalWindow.setContent(addFitemFragment);
									modalWindow.show(target);
								}
							});
							item.add(new AjaxLink<Void>("delete"){
								@Override
								public void onClick(AjaxRequestTarget target) {
									fItemService.delete(fItem);
									replaceThis(target);
								}
								@Override
								protected IAjaxCallDecorator getAjaxCallDecorator() {
									return new ConfiirAjaxPreProcessCallDecorator("Delete FItem?",super.getAjaxCallDecorator());
								}
							});
						}
				    });
				}
			});
			add(new AjaxLink<Void>("addNs"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					AddFitemFragment  addFitemFragment= new AddFitemFragment(modalWindow.getContentId(), ns){
						@Override
						protected void doAfterSave(AjaxRequestTarget target) {
							super.doAfterSave(target);
							modalWindow.close(target);
							replaceThis(target);
						}
					};
					modalWindow.setContent(addFitemFragment);
					modalWindow.show(target);
				}
			});
		}
		
		private void replaceThis(AjaxRequestTarget  target){
			FItemFragment  newFiFragment= new FItemFragment(FItemFragment.this.getId(), nsService.getById(ns.getId()));
			FItemFragment.this.replaceWith(newFiFragment);
			target.addComponent(newFiFragment);
		}
	}
	
	
	private class ConfiirAjaxPreProcessCallDecorator extends  AjaxPreprocessingCallDecorator {
		String message;
		public ConfiirAjaxPreProcessCallDecorator(String message ,IAjaxCallDecorator  iAjaxCallDecorator) {
			super(iAjaxCallDecorator);
			this.message=message;
		}
		@Override
		public CharSequence preDecorateScript(CharSequence script) {
			return "if(!confirm('" + message+ "')) return false;" + script;
		}
	}
	
	private class EditUserFragment  extends  Fragment {
		public EditUserFragment(String id, User  user) {
			super(id, "editUserFragment", HomePage.this);
			setOutputMarkupId(true);
			Form<User>  form= new Form<User>("form", new CompoundPropertyModel<User>(user));
			add(form);
			form.add(new TextField<String>("firstName"));
			form.add(new TextField<String>("lastName"));
			form.add(new TextField<String>("phoneNo"));
			form.add(new TextField<String>("emailAddress"));
			form.add(new TextField<String>("username"));
			form.add(new AjaxButton("save",form) {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					User  user=(User)form.getModelObject();
					userService.createOrUpdate(user);
					replaceThis(target);
				}
			});
			form.add(new AjaxButton("cancel"){
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					replaceThis(target);
				}
			});
		}
		protected void replaceThis(AjaxRequestTarget  target){
			UsersListsFragment  usersListsFragment= new UsersListsFragment(CONTENT_ID);
			EditUserFragment.this.replaceWith(usersListsFragment);
			target.addComponent(usersListsFragment);
		}
		
	}
	
	private  class UsersListsFragment extends  Fragment{
		public UsersListsFragment(String id) {
			super(id, "usersListsFragment", HomePage.this);
			setOutputMarkupId(true);
			List<IColumn<User>>   columns= Lists.newArrayList();
			columns.add(new PropertyColumn<User>(new Model<String>("First Name"), "firstName"));
			columns.add(new PropertyColumn<User>(new Model<String>("Last Name"), "lastName"));
			columns.add(new AbstractColumn<User>(new Model<String>("Action")) {
				@Override
				public void populateItem(Item<ICellPopulator<User>> cellItem,
						String componentId, IModel<User> rowModel) {
					cellItem.add(new EditUserLinkFragment(componentId, rowModel.getObject()));
				}
			});
			SortableDataProvider<User>  dataProvider= new SortableDataProvider<User>() {
				@Override
				public Iterator<? extends User> iterator(int first, int count) {
					userSearchDTO.getPaginationDTO().setFirst(first);
					userSearchDTO.getPaginationDTO().setCount(count);
					return userService.findUser(userSearchDTO).iterator();
				}
				@Override
				public IModel<User> model(User user) {
					return new Model<User>(user);
				}
				@Override
				public int size() {
					return userService.getUserCount(userSearchDTO);
				}
			};
			add(new AjaxFallbackDefaultDataTable<User>("datatable",columns,dataProvider, NSUtil.RECORDS_PER_PAGE));
			add(new AjaxLink<Void>("newUser"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					EditUserFragment  editUserFragment= new EditUserFragment(CONTENT_ID, new User());
					UsersListsFragment.this.replaceWith(editUserFragment);
					target.addComponent(editUserFragment);
				}
			});
			add(new AjaxLink<Void>("nsLists"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					NsListsFragment  nsListsFragment= new NsListsFragment(CONTENT_ID);
					UsersListsFragment.this.replaceWith(nsListsFragment);
					target.addComponent(nsListsFragment);
				}
			});

		}
	}
	
	private class EditUserLinkFragment  extends  Fragment{
		public EditUserLinkFragment(String id, final User  user  ) {
			super(id, "editUserLinkFragment", HomePage.this);
			setOutputMarkupId(true);
			add(new AjaxLink<Void>("link") {
				@Override
				public void onClick(AjaxRequestTarget target) {
					EditUserFragment  editUserFragment= new EditUserFragment(CONTENT_ID, user);
					getPage().get(CONTENT_ID).replaceWith(editUserFragment);
					target.addComponent(editUserFragment);
				}
			});
		}
	}
	
	
	private class AddFitemFragment  extends  Fragment {
		public AddFitemFragment(String id, final Ns  ns) {
			super(id, "addFitemFragment", HomePage.this);
			Form<FItem>  form= new Form<FItem>("form", new CompoundPropertyModel<FItem>(getFItem()));
			add(form);
			form.add(new TextField<String>("quantity"));
			form.add(new DropDownChoice<FType>("ftType",Lists.newArrayList(FType.values()),new IChoiceRenderer<FType>() {
				@Override
				public Object getDisplayValue(FType object) {
					return object.getDisplayName();
				}
				@Override
				public String getIdValue(FType object, int index) {
					return String.valueOf(index);
				}
			}));
			form.add(new DropDownChoice<User>("user",userService.getAll(), new IChoiceRenderer<User>(){
				public Object getDisplayValue(User object) {
					return NSUtil.formatUser(object);
				};
				public String getIdValue(User object, int index) {
					return String.valueOf(index);
				};
			}));
			form.add(new AjaxButton("save"){
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					FItem  fItem= (FItem)form.getModelObject();
					fItem.setNs(ns);
					fItemService.createOrUpdate(fItem);
					doAfterSave(target);
				}
			});
			form.add(new AjaxButton("cancel"){
				{
					setDefaultFormProcessing(false);
				}
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					doAfterSave(target);
				}
			});
		}
		protected void doAfterSave(AjaxRequestTarget  target){};
		protected FItem  getFItem(){
			return new FItem();
		}
	}
}
