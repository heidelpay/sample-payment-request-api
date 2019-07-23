package com.heidelpay.samples.paymentapp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * 
 * Controller rendering the manifests and the service worker, providing the head request and the html for a custom payment method.
 */
@Controller
public class PaymentApplicationController {

	/**
	 * the head request.
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(path="heidelpay-invoice", method = {RequestMethod.HEAD})
	public void head(HttpServletResponse response) throws IOException {
		String url = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("heidelpay-invoice/paymentmanifest.json")
				.build(true).toString();
		response.addHeader("Link", "<" + url + ">; rel=\"payment-method-manifest\"");
		response.setStatus(200);
		response.getWriter().append("Link:<"+url+">; rel=\"payment-method-manifest\\\"");
	}
	
	/**
	 * @return the payment manifest.
	 */
	@GetMapping(path = "heidelpay-invoice/paymentmanifest.json", produces="application/json")
	public @ResponseBody Map<String, String[]> definePaymentMethods() {
		Map<String, String[]> manifest = new HashMap<>();
		String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("").build(true).toString();
		String[] methods = new String[] {baseUrl + "/heidelpay-invoice/webappmanifest.json"};
		String[] supportedOrigins = new String[] {baseUrl, "http://localhost:8080"};
		manifest.put("default_applications", methods);
		manifest.put("supported_origins", supportedOrigins);
		return manifest;
	}
	
	/**
	 * 
	 * @return the service worker
	 */
	@GetMapping(path = "heidelpay-invoice/heidelpay-invoice-service-worker.js", produces="text/javascript")
	public ModelAndView renderServiceWorker() {
		ModelAndView modelAndView = new ModelAndView("heidelpay/invoice/service-worker.js");
		modelAndView.addObject("origin", ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("").build(false).toString());
		
		return modelAndView;
	}
	
	/**
	 * 
	 * @return the form used for the demonstration of the payment method.
	 */
	@GetMapping(path = "heidelpay-invoice/checkout")
	public ModelAndView paymentDetails() {
		ModelAndView modelAndView = new ModelAndView("heidelpay/invoice/detail.html");
		return modelAndView;
	}
	
}
