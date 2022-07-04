const { createApp } = Vue

createApp({
    data() {
        return {
            token: document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        }
    }
}).mount('#app')