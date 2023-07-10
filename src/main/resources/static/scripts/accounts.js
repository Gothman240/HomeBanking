const { createApp } = Vue;

createApp({
    data(){
        return {
            showSide: false,
            client: [],
            accounts: [],
            loans: [],
            totalBalance: 0,
            showBal: true,
        }
    },
    created(){
        this.loadData();
        this.showSide = JSON.parse(localStorage.getItem("sideBar"));
        this.showBal = JSON.parse(localStorage.getItem("eyeBalance"))
        
    },
    methods: {
        showSideBar(){
            this.showSide = !this.showSide;
            localStorage.setItem("sideBar", JSON.stringify(this.showSide));
        },
        loadData(){
            axios.get("http://localhost:8080/api/clients/current")
            .then(response  => {
                this.client = response.data;
                console.log(response)
                this.accounts = response.data.accounts;
                this.loans = response.data.loans
                this.accounts.sort((a, b) => a.id - b.id);
                this.totalBalance = this.accounts.reduce((acc, item) => {
                    return acc + item.balance
                }, 0)
                
            })
            .catch(err => console.log(err))
        },
        showBalance(){
            this.showBal = !this.showBal;
            localStorage.setItem("eyeBalance", JSON.stringify(this.showBal));
        },
        formatCurrency(currency){
            const format = new Intl.NumberFormat('en-US', {
                style: 'currency',
                currency: 'USD',
            })

            return format.format(currency)
        },
        loanMonthlyCalculator(amount, payments){
            return amount / payments;
        },
        logout(){
            axios.post("/api/logout")
            .then(res => {
                if(res.status === 200){
                    window.location.href = "/login.html"
                }
            })
        },
        createAccount(){
            axios.post("/api/accounts", "balance=0.0" ,  { headers: { "content-type": "application/x-www-form-urlencoded" } })
            .then(res => {
                this.loadData()
            })
            .catch(err => console.log(err.toJSON()))
        }

    },
    computed: {
       
    }
}).mount("#app")