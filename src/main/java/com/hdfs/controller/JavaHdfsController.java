package com.hdfs.controller;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hdfs.service.JavaHdfsService;

@Controller
public class JavaHdfsController {
    
    private Configuration conf = new Configuration();
    
    @Autowired
    private JavaHdfsService javaHdfsService;
    
    public JavaHdfsController(){
	// default Constructor
    }
    
    /**
     * 하둡 디렉토리 구조 조회  - 메인 화면
     * 
     * @param requestMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hdfsHome")
    public ModelAndView hdfsHome(Object requestMap) throws Exception {
	ModelAndView mav = new ModelAndView("/hdfs/home");
	
	mav.addObject("pageName", "HDFS Test page");
	
	return mav;
    }

    /**
     * 하둡 경로 정보 조회(폴더, 파일, 파일내용)
     * 
     * @param requestMap
     * @throws Exception
     */
    @RequestMapping(value = "/hdfsInfo")
    public ModelAndView hdfsInfo(Object requestMap) throws Exception {
	Map<String, Object> pMap = (Map<String, Object>) requestMap;
	
	String hdfsDirPath = (String) pMap.get("data");
	if(hdfsDirPath == null || hdfsDirPath.equals("")){
            hdfsDirPath = "/";
        }
	Map<String, Object> map = javaHdfsService.hdfsGetInfo(hdfsDirPath);
	
	ModelAndView mav = new ModelAndView("jsonView");
	mav.addObject("result", map);
	
	return mav;
    }
    
    /**
     * 하둡 경로 폴더 생성
     * 
     * @param requestMap
     * @return
     */
    @RequestMapping(value = "/mkdir")
    public ModelAndView hdfsMkdir(Object requestMap) throws Exception {
	Map<String, Object> pMap = (Map<String, Object>) requestMap;
	String dirName = (String) pMap.get("data");
	
	String result = javaHdfsService.hdfsMkdirService(dirName);
	
	ModelAndView mav = new ModelAndView("jsonView");
	mav.addObject("msg", result);
	
	return mav;
    }
    
    
    /**
     * 하둡경로 폴더 / 파일 삭제
     * 
     * @param requestMap
     * @return
     */
    @RequestMapping(value = "/deldir")
    public ModelAndView hdfsDelete(Object requestMap) throws Exception {
	Map<String, Object> pMap = (Map<String, Object>) requestMap;
	String dirName = (String) pMap.get("data");
	
	String result = javaHdfsService.hdfsDeleteService(dirName);
	
	ModelAndView mav = new ModelAndView("jsonView");
	mav.addObject("msg", result);
	
	return mav;
    }
    
    /**
     * 하둡 경로에 파일 업로드
     *  - 추가하고자 하는 파일 목록을 이동한 하둡 폴더 경로에 저장
     *  - JavaHdfs 의 copyFromLocalFile(srcPath, dstPath) 메소드를 이용한 업로드
     * 
     * @param requestMap	파일을 저장하기 위한 하둡 경로 값
     * @param request		저장하고자 하는 파일 목록
     * @return			저장한 결과 값
     * @throws URISyntaxException 
     */
    @RequestMapping(value = "/hdfsPutFiles")
    public ModelAndView hdfsPutFiles(Object requestMap, HttpServletRequest request) throws Exception {
	Map<String, Object> pMap = (Map<String, Object>) requestMap;
	String uploadPath = (String) pMap.get("uploadPath");
	
	MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
	String result = javaHdfsService.hdfsPutFilesService(uploadPath, multipartHttpServletRequest);
	
	ModelAndView mav = new ModelAndView("jsonView");
	
	mav.addObject("result", result);
	
	return mav;
    }
    
    /**
     * 하둡 경로에 업로드한 파일 삭제
     *  - 단일 삭제만 가능
     *  - 폴더 삭제 불가! 파일만 삭제 가능
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public ModelAndView hdfsRemoveFile(Object requestMap, HttpServletRequest request) throws Exception {
	Map<String, Object> pMap = (Map<String, Object>) requestMap;
	String dirName = (String) pMap.get("data");
	
	String result = javaHdfsService.hdfsDeleteService(dirName);
	
	ModelAndView mav = new ModelAndView("jsonView");
	mav.addObject("msg", result);
	
	return mav;
    }
    
    /**
     * 하둡 경로의 파일 다운로드
     *  - JavaHdfs의 {???} 메소드를 이용한 다운로드
     * 
     * @param requestMap	하둡경로, 다운로드할 파일명
     * @return			다운로드 결과 값
     */
    public ModelAndView hdfsGetFiles(Object requestMap, HttpServletResponse response) {
	ModelAndView mav = new ModelAndView("jsonView");
	
	return mav;
    }
}