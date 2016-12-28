package com.gp.web.square;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.gp.core.PostFacade;
import com.gp.core.SecurityFacade;
import com.gp.dao.info.PostCommentInfo;
import com.gp.svc.info.PostExt;
import com.gp.svc.info.UserLiteInfo;
import com.gp.web.model.Comment;
import com.gp.web.model.PostItem;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import com.gp.audit.AccessPoint;
import com.gp.common.GeneralConfig;
import com.gp.common.Principal;
import com.gp.common.SystemOptions;
import com.gp.core.WorkgroupFacade;
import com.gp.exception.CoreException;
import com.gp.pagination.PageQuery;
import com.gp.pagination.PageWrapper;
import com.gp.pagination.PaginationInfo;
import com.gp.svc.info.WorkgroupLite;
import com.gp.util.DateTimeUtils;
import com.gp.web.BaseController;
import com.gp.web.model.Workgroup;

@Controller
@RequestMapping("/square")
public class AllSquarePostsController extends BaseController{

	static Logger LOGGER = LoggerFactory.getLogger(AllSquarePostsController.class);
	
	static String ImagePath = GeneralConfig.getString(SystemOptions.IMAGE_CACHE_PATH);
	
	@RequestMapping("all-posts")
	public ModelAndView doGridInitial(HttpServletRequest request) throws UnsupportedEncodingException{

		ModelAndView mav = getJspModelView("square/all-posts");
		
		return mav;
	}
	
	
	@RequestMapping("all-posts-next")
	public ModelAndView doAllPostsNextLoad(HttpServletRequest request)  throws CoreException{

		ModelAndView mav = super.getJspModelView("square/all-posts-next");

		PageQuery pquery = new PageQuery(4,1);

		Principal principal = super.getPrincipal();
		AccessPoint accesspoint = super.getAccessPoint(request);

		String state = super.readRequestParam("state");
		String type = super.readRequestParam("type");
		String scope = super.readRequestParam("scope");
		super.readRequestData(pquery);

		PageWrapper<PostExt> presult = PostFacade.findSquarePosts(
				accesspoint, principal, state, type, scope, pquery
		);

		List<PostExt> entries = presult.getRows();
		if(CollectionUtils.isEmpty(entries)){
			mav.addObject("entries", entries);
			mav.addObject("hasMore", false);
			mav.addObject("nextPage", 1);
			mav.addObject("state", state);
			mav.addObject("type", type);
			mav.addObject("scope", scope);
			return mav;
		}
		List<PostItem> items = new ArrayList<>();
		Set<String> accounts = new HashSet<String>();
		for(PostExt cinfo : entries){

			PostItem post = new PostItem();
			post.setState(cinfo.getState());
			post.setOwner(cinfo.getOwner());
			post.setClassification(cinfo.getClassification());
			post.setOwnerName(cinfo.getOwnerName());
			post.setCommentOn(cinfo.isCommentOn());
			post.setContent(cinfo.getContent());
			post.setExcerpt(cinfo.getExcerpt());
			post.setPostId(cinfo.getInfoId().getId());
			post.setSubject(cinfo.getSubject());
			post.setPostTime(cinfo.getPostDate().toString());
			post.setPriority(String.valueOf(cinfo.getPriority()));

			accounts.add(cinfo.getOwner());

			// find the comments and attach them to post.
			List<PostCommentInfo> cinfos = PostFacade.findPostComments(accesspoint,principal,
					cinfo.getInfoId(), null, null);

			List<Comment> comments = new ArrayList<>();
			// prepare to fetch the author avatar etc
			for(PostCommentInfo cmtinfo: cinfos){

				accounts.add(cmtinfo.getAuthor());

				Comment cmt = new Comment();
				cmt.setState(cmtinfo.getState());
				cmt.setAuthor(cmtinfo.getAuthor());
				cmt.setCommentDate(DateTimeUtils.toDateTime(cmtinfo.getCommentDate()));
				cmt.setContent(cmtinfo.getContent());
				cmt.setPostId(cmtinfo.getPostId());

				comments.add(cmt);
			}
			post.setComments(comments);
			// set the attendees
			List<UserLiteInfo> attendees = PostFacade.findPostAttendees(accesspoint, principal,
					cinfo.getInfoId());
			for(UserLiteInfo ulite : attendees){
				// decorate the link of image
				ulite.setAvatarLink("../"+ImagePath + "/" + ulite.getAvatarLink());
			}
			post.setAttendees(attendees);

			// add post to item list
			items.add(post);
		}
		List<String> userlist = new ArrayList<>();
		userlist.addAll(accounts);
		List<UserLiteInfo> allusers = SecurityFacade.findAccountLites(accesspoint, principal,
				null, userlist);
		Map<String, UserLiteInfo> allmap = new HashMap<>();
		for(UserLiteInfo ulite : allusers){

			allmap.put(ulite.getAccount(), ulite);
		}

		for(PostItem item : items){
			// set owner avatar link
			item.setOwnerAvatar("../"+ImagePath + "/" + allmap.get(item.getOwner()).getAvatarLink());
			for(Comment comment: item.getComments()){

				comment.setAuthorAvatar("../"+ImagePath + "/" + allmap.get(comment.getAuthor()).getAvatarLink());
			}
		}

		mav.addObject("entries", items);
		mav.addObject("hasMore", items.size() == 4);
		mav.addObject("nextPage", pquery.getPageNumber() + 1);
		mav.addObject("state", state);
		mav.addObject("type", type);
		mav.addObject("scope", scope);

		return mav;
	}

}
