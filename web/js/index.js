new Vue({
    el: '#app',
    data: {
        me: {}
    },
    methods: {
        getMe() {
            apiClient.get(`/users/me`).then(response => {
                this.me = response.data;
            });
        }
    },
    mounted() {
        this.getMe();
    }
});