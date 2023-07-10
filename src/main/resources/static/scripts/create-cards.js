const { createApp } = Vue;

createApp({
  data() {
    return {
        showSide: false,
        client: {},
        type: "",
        color: "",
    };
  },
  created() {
    this.getClient()
    this.showSide = JSON.parse(localStorage.getItem("sideBar"));
  },
  methods: {
    getClient(){
        axios.get("http://localhost:8080/api/clients/current")
            .then(response  => {
                this.client = response.data;
                console.log(this.client)
                
                
            }).catch(err => console.log(err))
    },
    showSideBar(){
        this.showSide = !this.showSide;
        localStorage.setItem("sideBar", JSON.stringify(this.showSide));
    },
    logout(){
        axios.post("/api/logout")
        .then(res => {
            if(res.status === 200){
                window.location.href = "/login.html"
            }
        }).catch(err => console.log(err.toJSON()))
    },
    tryCreateCard(){
        axios.post("/api/cards", `type=${this.type}&color=${this.color}`,
        { headers: { "content-type": "application/x-www-form-urlencoded" } })
            .then(response  => {

                console.log("send")
                this.showAlert(response.data, success)
                setTimeout(() => {
                    location.href="./../web/cards.html";                    
                }, 2000);
                
            })
            .catch(err => {
                this.showAlert(err.response.data, "info")
            })
    },
    colorCard(){
        if(this.color === "TITANIUM"){
            return "grey"
        }
        if(this.color === "GOLD"){
            return "yellow"
        }
        if(this.color === "SILVER"){
            return "grey"
        }
    },
    secondaryColorCard(){
        if(this.color === "TITANIUM"){
            return "greydark"
        }
        if(this.color === "GOLD"){
            return "limedark"
        }
        if(this.color === "SILVER"){
            return "grey"
        }
    },
    showAlert(status, type){
        let alerttype = type;
        if(alerttype==="success"){
          toastr.success(status);
        }
        
        if(alerttype==="info"){
          toastr.info(status);
        }
      }
  },
  computed: {},
}).mount("#app");
