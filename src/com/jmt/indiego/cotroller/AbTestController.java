package com.jmt.indiego.cotroller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jmt.indiego.service.AbChoiceService;
import com.jmt.indiego.service.AbTestService;
import com.jmt.indiego.util.FileRenameUtil;
import com.jmt.indiego.vo.AbChoice;
import com.jmt.indiego.vo.AbTest;
import com.jmt.indiego.vo.User;

@Controller
public class AbTestController {

	private AbTestService abTestService;
	private AbChoiceService abChoiceService;
	private FileRenameUtil fileRenameUtil;

	public void setFileRenameUtil(FileRenameUtil fileRenameUtil) {
		this.fileRenameUtil = fileRenameUtil;
	}

	public void setAbTestService(AbTestService abTestService) {
		this.abTestService = abTestService;
	}

	public void setAbChoiceService(AbChoiceService abChoiceService) {
		this.abChoiceService = abChoiceService;
	}

	@RequestMapping(value = "/abTest/main", method = RequestMethod.GET)
	public String abTestmain(Model model, HttpSession session) {
		return "abTest_main_page";
	}

	@RequestMapping(value = "/abTest/main", method = RequestMethod.GET)
	public Map<String, Object> abTestmain(HttpSession session) {
		return abTestService.mainABTest(session);
	}

	@RequestMapping(value = "/ajax/abTest/main/page/{pageNo}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getIndexContents(@PathVariable int pageNo) {
		return abTestService.getAllAbTest(pageNo);
	}

	@RequestMapping(value = "/abTest/upload", method = RequestMethod.GET)
	public String writeForm() {
		return "abTest_upload";
	}

	@RequestMapping(value = "/abTest/{no}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable int no) {
		return "abTest_detail";
	}

	@RequestMapping(value = "/abTest/{no}/detail", method = RequestMethod.GET)
	public Map<String, Object> detail(@PathVariable int no, HttpSession session) {
		return abTestService.detailABTest(no, session);
	}

	@RequestMapping(value = "/abTest/upload", method = RequestMethod.POST)
	public String upload(AbTest abTest, HttpSession session) {
		User user = (User) session.getAttribute("loginUser");
		abTest.setUserNo(user.getUserNo());
		boolean result = abTestService.addAbTest(abTest);
		System.out.println(result);
		return "redirect:/abTest/" + abTest.getNo() + "/detail";
	}

	// json view
	// model no needed
	@RequestMapping(value = "/ajax/image/upload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String uploadImg(MultipartFile image, HttpServletRequest request) { // ?????? name??? ??????
		System.out.println("POST /upload");
		System.out.println(image);

		// WAS??????
		String rootPath = request.getServletContext().getRealPath("/");

//		System.out.println(rootPath);

		String uploadPath = rootPath + "img" + File.separator + "abTest" + File.separator;
		System.out.println(uploadPath);

		String fileName = image.getOriginalFilename();

		// ?????????????????? ??????????????? ???????????????????????????.
		System.out.println(fileName);

		// ????????? ??????
		File file = new File(uploadPath + fileName);

		// ????????? ?????? ????????? ???????????? ????????? ?????? ?????????.
		file = fileRenameUtil.rename(file);

		try {
			// ????????? ????????? ????????? ???????????? ?????????.
			// ????????????
			image.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} // try~catch end

		return "{\"src\":\"" + file.getName() + "\"}";
	}

	@RequestMapping(value = "/abChoice/AB", method = RequestMethod.POST)
	public String chooseAB(AbChoice abChoice, HttpSession session) {

		User user = (User) session.getAttribute("loginUser");
		abChoice.setUserNo(user.getUserNo());
		int result = abChoiceService.addChoice(abChoice);
		System.out.println(result);
		return "redirect:/abTest/main";
	}

	@RequestMapping(value = "/abChoice/edit", method = RequestMethod.PUT)
	public String updateChoice(AbChoice abChoice, HttpSession session) {
		System.out.println("chooseAB");
		User user = (User) session.getAttribute("loginUser");
		abChoice.setUserNo(user.getUserNo());
		System.out.println(user.getUserNo());
		System.out.println(abChoice.getChoice());
		System.out.println(abChoice.getContentsNo());
		boolean result = abChoiceService.editChoice(abChoice);
		System.out.println(result);
		return "redirect:/abTest/main";
	}

}
