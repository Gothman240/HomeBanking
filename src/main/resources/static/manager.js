const {createApp} = Vue;

createApp({
    data(){
        return{
            postSuccess: false,
            json: "",
            clients: [],
            form: {
                firstName: "",
                lastName: "",
                email: ""
            },
            client: [],
        };
    },
    created() {
        this.loadData();
    },
    methods: {
        loadData(){
        axios.get('http://localhost:8080/clients')
        .then((response) => {
            
            this.clients = response.data._embedded.clients;
            this.json = response.data;
            console.log(response)
            console.log(this.clients)
            console.log(this.client)
        })
        .catch(err => console.error(err))
        },
        addData(){
            this.client.push({... this.form})

            if(this.form.firstName && this.form.lastName && this.form.email){
                this.postClient()
            }

        },
        postClient(){
            axios.post('http://localhost:8080/clients', this.form)
            .then((response) => {
                

                this.loadData()

                this.postSuccess=true;
                setTimeout(() => {
                    this.postSuccess=false
                }, 3000);

                this.form.firstName = "";
                this.form.lastName= "";
                this.form.email= "";
            })
            .catch(err => console.log(err))
        },
        deleteClient(id){
            axios.delete(id)
            .then(response => {
                this.loadData()
            })
        }

    },
    computed: {
        

    }
}).mount("#app");