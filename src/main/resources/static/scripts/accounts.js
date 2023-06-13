const {createApp} = Vue;

createApp({
    data(){
        return {
            show: false,
            client: [],
            accounts: [],
            totalBalance: 0,
        }
    },
    created(){
        this.loadData();
    },
    methods: {
        showSideBar(){
            this.show = !this.show;
        },
        loadData(){
            axios.get("http://localhost:8080/api/clients/1")
            .then(response  => {
                this.client = response.data;
                this.accounts = response.data.accounts;
                this.accounts.sort((a, b) => a.id - b.id);
                this.totalBalance = this.accounts.reduce((acc, item) => {
                    return acc + item.balance
                }, 0)
                console.log(this.total)
            })
            .catch(err => console.log(err))
        }
    },
    computed: {
       
    }
}).mount("#app")