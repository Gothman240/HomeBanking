const { createApp } = Vue;

createApp({
  data() {
    return {
        showSide: false,
        client: [],
        accounts: [],
        account: [],
        type: "",
        color: "",
        currentStep: 1,
        formData: {
          originNumber:"",
          destinyNumber: "",
          amount:0.0,
          description:"",
        }
    };
  },
  created() {
    this.getClient();
  },
  methods: {
    getClient(){
        axios.get("http://localhost:8080/api/clients/current")
            .then(response  => {
                this.client = response.data;
                this.accounts = response.data.accounts;
                
                
            }).catch(err => console.log(err))
    },
    showSideBar(){
        this.showSide = !this.showSide;
    },
    logout(){
        axios.post("/api/logout")
        .then(res => {
            if(res.status === 200){
                window.location.href = "/login.html"
            }
        }).catch(err => console.log(err.toJSON()))
    },
    
    nextStep() {
        if (this.currentStep < 4) {
          this.currentStep++;
        }
      },
      prevStep() {
        if (this.currentStep > 1) {
          this.currentStep--;
        }
        if(this.currentStep == 2){
          
          console.log(this.account)
        }
      },
      showAlert(status, type){
        let alerttype = type;
        if(alerttype==="success"){
          toastr.success(status);
        }
        
        if(alerttype==="warning"){
          toastr.warning(status);
        }
      },
      submitForm() {
        axios.post("/api/transactions" , `amount=${this.formData.amount}&description=${this.formData.description}&originNumber=${this.formData.originNumber}&destinyNumber=${this.formData.destinyNumber}`,  { headers: { "content-type": "application/x-www-form-urlencoded" } })
        .then(response => {
          console.log(response)
          this.showAlert(response.data, "success")
        }).catch(err => { 
          console.log(err)
          this.showAlert(err.response.data, "warning")
        })
      },
      
  },
  computed: {
    progressWidth() {
        return ((this.currentStep - 1) / 3) * 100 + "%";
      },
      
      showAccount(){
        return this.accounts.filter(account => account.number === this.formData.originNumber)
    },
    showBalance(){
      if(this.currentStep == 2){
        this.account = this.accounts.filter( account => account.balance == this.formData.originNumber)
        console.log(this.account)
      }
    },
    
  },
}).mount("#app");

  