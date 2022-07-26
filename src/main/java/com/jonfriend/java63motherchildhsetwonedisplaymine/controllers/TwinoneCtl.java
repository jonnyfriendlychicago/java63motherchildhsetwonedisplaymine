package com.jonfriend.java63motherchildhsetwonedisplaymine.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jonfriend.java63motherchildhsetwonedisplaymine.models.TwintwoMdl;
import com.jonfriend.java63motherchildhsetwonedisplaymine.models.UserMdl;
import com.jonfriend.java63motherchildhsetwonedisplaymine.models.HouseMdl;
import com.jonfriend.java63motherchildhsetwonedisplaymine.models.TwinoneMdl;
import com.jonfriend.java63motherchildhsetwonedisplaymine.services.TwintwoSrv;
import com.jonfriend.java63motherchildhsetwonedisplaymine.services.HouseSrv;
import com.jonfriend.java63motherchildhsetwonedisplaymine.services.TwinoneSrv;
import com.jonfriend.java63motherchildhsetwonedisplaymine.services.UserSrv;

@Controller
public class TwinoneCtl {

	@Autowired
	private TwinoneSrv twinoneSrv;
	
	@Autowired
	private TwintwoSrv twintwoSrv;
	
	@Autowired
	private UserSrv userSrv;
	
	@Autowired
	private HouseSrv houseSrv;
	
	// display create-new page
	@GetMapping("/twinone/new")
	public String newTwinone(
			@ModelAttribute("twinone") TwinoneMdl twinoneMdl
			, Model model
			, HttpSession session
			) {
		 
		// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
		if(session.getAttribute("userId") == null) {return "redirect:/logout";}
		
		// We get the userId from our session (we need to cast the result to a Long as the 'session.getAttribute("userId")' returns an object
		Long userId = (Long) session.getAttribute("userId");
		model.addAttribute("user", userSrv.findById(userId));
		
		// records in house dropdown
		List<HouseMdl> intVar1 = houseSrv.returnAll();
		model.addAttribute("houseList", intVar1);    
		
		return "twinone/create.jsp";
	}
	 
	// process the create-new  
	@PostMapping("/twinone/new")
	public String addNewTwinone(
			@Valid @ModelAttribute("twinone") TwinoneMdl twinoneMdl
			, BindingResult result
			, Model model
			, HttpSession session
			) {
		
		// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
		if(session.getAttribute("userId") == null) {return "redirect:/logout";}
		
		// We get the userId from our session (we need to cast the result to a Long as the 'session.getAttribute("userId")' returns an object
		Long userId = (Long) session.getAttribute("userId");
		model.addAttribute("user", userSrv.findById(userId));
		
		if(result.hasErrors()) {
			return "twinone/create.jsp";
		}else {

			// this gets the userModel object by calling the user service with the seeion user id
			UserMdl currentUserMdl = userSrv.findById(userId);
			// below sets the userId of the new record with above acquisition.
			twinoneMdl.setUserMdl( currentUserMdl);
			
			twinoneSrv.create(twinoneMdl);
			
			return "redirect:/home";
		}
	}
	
	// view record
	@GetMapping("/twinone/{id}")
	public String showTwinone(
			@PathVariable("id") Long id
			, Model model
			, HttpSession session
			) {
		
		// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
		if(session.getAttribute("userId") == null) {return "redirect:/logout";}
		
		// We get the userId from our session (we need to cast the result to a Long as the 'session.getAttribute("userId")' returns an object
		Long userId = (Long) session.getAttribute("userId");
		model.addAttribute("user", userSrv.findById(userId));
		
		TwinoneMdl intVar = twinoneSrv.findById(id);
		
		model.addAttribute("twinone", intVar);
		model.addAttribute("assignedCategories", twintwoSrv.getAssignedTwinones(intVar));
		model.addAttribute("unassignedCategories", twintwoSrv.getUnassignedTwinones(intVar));
		
		return "twinone/record.jsp";
	}
	
	// display edit page
	@GetMapping("/twinone/{id}/edit")
	public String editTwinone(
			@PathVariable("id") Long twinoneId
			, Model model
			, HttpSession session
			) {
		
		// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
		if(session.getAttribute("userId") == null) {return "redirect:/logout";}

		// We get the userId from our session (we need to cast the result to a Long as the 'session.getAttribute("userId")' returns an object
		Long userId = (Long) session.getAttribute("userId");
		model.addAttribute("user", userSrv.findById(userId));
		
		// pre-populates the values in the management interface
		TwinoneMdl intVar = twinoneSrv.findById(twinoneId);
		
		// records in house dropdown
		List<HouseMdl> intVar1 = houseSrv.returnAll();
		model.addAttribute("houseList", intVar1);  
		
		model.addAttribute("twinone", intVar);
		model.addAttribute("assignedCategories", twintwoSrv.getAssignedTwinones(intVar));
		model.addAttribute("unassignedCategories", twintwoSrv.getUnassignedTwinones(intVar));
		
		// records in 'manage-one' interface dropdown
//		List<DojoMdl> intVar3 = dojoSrvIntVar.returnAll();
//		model.addAttribute("dojoList", intVar3); 
		
		return "twinone/edit.jsp";
	}
	
	// process the edit(s)
//	@PostMapping("/twinone/{id}/edit")
	// line above replaced by below.  we used to include the path variable id of the record, but we've side stepped that with the ModAtt approach.
	@PostMapping("/twinone/edit")
	public String PostTheEditTwinone(
			@Valid 
			@ModelAttribute("twinone") TwinoneMdl twinoneMdl 
			, BindingResult result
			, Model model
//			, @PathVariable("id") Long twinoneId
			// above no longer needed, b/c we are using modAtt approach.
			, HttpSession session
			, RedirectAttributes redirectAttributes
			) {
		
		// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
		if(session.getAttribute("userId") == null) {return "redirect:/logout";}
		
		// trying here to stop someone from forcing this method when not creator; was working, now no idea.... sigh 7/19 2pm
//		 Long userId = (Long) session.getAttribute("userId"); 
//		 PublicationMdl intVar = twinoneSrv.findById(twinoneId);
		
		// System.out.println("in the postMapping for edit..."); 
		// System.out.println("intVar.getUserMdl().getId(): " + intVar.getUserMdl().getId()); 
		// System.out.println("userId: " + userId); 
		
//		if(intVar.getUserMdl().getId() != userId) {
//			redirectAttributes.addFlashAttribute("mgmtPermissionErrorMsg", "Only the creator of a record can edit it.");
//			return "redirect:/publication";
//		}
		
		// below now setting up intVar object by using the getID on the modAtt thing. 
		TwinoneMdl intVar = twinoneSrv.findById(twinoneMdl.getId());
		
		if (result.hasErrors()) { 
			
            Long userId = (Long) session.getAttribute("userId");
            model.addAttribute("user", userSrv.findById(userId));            
//            model.addAttribute("twinone", intVar);
            model.addAttribute("assignedCategories", twintwoSrv.getAssignedTwinones(intVar));
            model.addAttribute("unassignedCategories", twintwoSrv.getUnassignedTwinones(intVar));

			return "twinone/edit.jsp";
		} else {
			
			// this merely returns the joined twintwo records list
			twinoneMdl.setTwintwoMdl(twintwoSrv.getAssignedTwinones(intVar));
			
			twinoneMdl.setUserMdl(intVar.getUserMdl());
			// translation of line above: we are reSETTING on the twinone model object/record the createbyid to that which is GETTING the creatingbyid from the DB... NO LONGER from that silly hidden input. 

			twinoneSrv.update(twinoneMdl);
			// below now setting up intVar object by using the getID on the modAtt thing. 
			return "redirect:/twinone/" + intVar.getId();
		}
	}
	
	// process new joins for that one twinone
	@PostMapping("/twinone/{id}/editTwintwoJoins")
	public String postTwinoneTwintwoJoin(
//			@PathVariable("id") Long id
			@PathVariable("id") Long twinoneId
			, @RequestParam(value="twintwoId") Long twintwoId // requestParam is only used with regular HTML form 
			,  Model model
			, HttpSession session
			) {
		
		// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
		if(session.getAttribute("userId") == null) {return "redirect:/logout";}
		
		// We get the userId from our session (we need to cast the result to a Long as the 'session.getAttribute("userId")' returns an object
		Long userId = (Long) session.getAttribute("userId");
		model.addAttribute("user", userSrv.findById(userId));
		
		TwinoneMdl twinone = twinoneSrv.findById(twinoneId);
		TwintwoMdl twintwo = twintwoSrv.findById(twintwoId);
		
		twinone.getTwintwoMdl().add(twintwo);
		
		twinoneSrv.update(twinone);
		
		// need these two below so that the returned page has this dropdown/table info populated.
		model.addAttribute("assignedCategories", twintwoSrv.getAssignedTwinones(twinone));
		model.addAttribute("unassignedCategories", twintwoSrv.getUnassignedTwinones(twinone));
//		return "redirect:/twinone/" + id;
		return "redirect:/twinone/" + twinoneId + "/edit";
	}
	
	@DeleteMapping("/removeTwinoneTwintwoJoin")
    public String removeTwinoneTwintwoJoin(
    		@RequestParam(value="twintwoId") Long twintwoId // requestParam is only used with regular HTML form
    		, @RequestParam(value="twinoneId") Long twinoneId // requestParam is only used with regular HTML form
    		// below removed, outmoded design
 //    		, @RequestParam(value="origin") Long originPath // requestParam is only used with regular HTML form
    		, HttpSession session
    		, RedirectAttributes redirectAttributes
    		) {

    	// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
		if(session.getAttribute("userId") == null) {return "redirect:/logout";}
		
		TwinoneMdl twinoneObject = twinoneSrv.findById(twinoneId);
		TwintwoMdl twintwoObject  = twintwoSrv.findById(twintwoId);
		
		twinoneSrv.removeTwinoneTwintwoJoin(twintwoObject, twinoneObject); 

//		if (originPath == 1) {
//			return "redirect:/twinone/" + twinoneId + "/edit";
//		} else {
//			return "redirect:/twintwo/" + twintwoId;
//		}
		
		// above replaced by below, above design outdated.
		
		return "redirect:/twinone/" + twinoneId + "/edit";
	}
	
	// delete twinone
    @DeleteMapping("/twinone/{id}")
    public String deleteTwinone(
    		@PathVariable("id") Long twinoneId
    		, HttpSession session
    		, RedirectAttributes redirectAttributes
    		) {
		// If no userId is found in session, redirect to logout.  JRF: put this on basically all methods now, except the login/reg pages
		if(session.getAttribute("userId") == null) {return "redirect:/logout";}
		
		// trying here to stop someone from forcing this method when not creator
		Long userId = (Long) session.getAttribute("userId"); 
		TwinoneMdl intVar = twinoneSrv.findById(twinoneId);
		
		// below is to prevent non-creator from deleting record
//		if(intVar.getUserMdl().getId() != userId) {
//			redirectAttributes.addFlashAttribute("mgmtPermissionErrorMsg", "Only the creator of a record can delete it.");
//			return "redirect:/publication";
//		}

		twinoneSrv.delete(intVar);
        return "redirect:/home";
    }
	

// end of ctl
}
