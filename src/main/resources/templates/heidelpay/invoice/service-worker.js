const origin = [[${origin}]];

const methodName = origin + "/heidelpay-invoice";
let checkoutURL = origin + "/heidelpay-invoice/checkout";


class PromiseResolver {
	constructor(resolve, reject) {
		this.resolve_ = resolve;
		this.reject_ = reject;
		this.promise = new Promise(function(resolve, reject) {
		    this.resolve_ = resolve;
		    this.reject_ = reject;
		  }.bind(this));	
	}
	resolve(response) {
		this.resolve_(response);
	}
	reject(response) {
		this.reject_(response);
	}
}


let resolver = new PromiseResolver();
let payment_request_event;
let client;
let paymentUI;


self.addEventListener('paymentrequest', e => {
  payment_request_event = e;

  e.respondWith(resolver.promise);
  
  e.openWindow(checkoutURL).then(client => {
	  if (client === null) {
		  resolver.reject('Failed to open window');
	  } else {
    	paymentUI = client;
	  }
	  
  }).catch(err => {
	  resolver.reject(err);
  });
});
	
 self.addEventListener('message', e => {
	 if (e.data === "payment_app_window_ready") {
		 sendPaymentRequest();
		 return;
	 }

	 if (e.data.completed) {
		 let response = {
			"details": e.data,
			"methodName": methodName
		 }
		 resolver.resolve(response);
	 
	 } else {
		 resolver.reject(e.data.message);
	 }
 });

const sendPaymentRequest = () => {
  if (!payment_request_event) return;

  clients.matchAll({
    includeUncontrolled: false,
    type: 'window'
  }).then(clientList => {
    for (let client of clientList) {	
      client.postMessage(payment_request_event.total);
    }
  });
}
