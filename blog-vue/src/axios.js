import axios from 'axios'
import Element from 'element-ui'
import router from './router'
import store from './store'

// 设置请求URL
axios.defaults.baseURL="http://localhost:9081";

// 设置前置拦截器
axios.interceptors.request.use(config => {
    // 可添加相关请求头信息
    return config;
});

// 后置拦截器
axios.interceptors.response.use(Response => {
        // 针对返回的内容做处理
        let res = Response.data;
        // 正常的请求情况，进行放行处理
        if (res.code === 200)
        {
            return Response;
        }

        Element.Message({
            showClose: true,
            message: res.messgae,
            type: 'error',
            duration: 3 * 1000
        });

        // 不在往下进行处理
        return Promise.reject(res.messgae);
    },
    error => {
        if (error.response.data)
        {
            error.messgae = error.response.data.messgae;
        }

        if (error.response.status === 401)
        {
            store.commit("REMOVE_USERINFO");
            router.push("/login");
        }

        Element.Message({
            showClose: true,
            message: error.messgae,
            type: 'error',
            duration: 3 * 1000
        });

        // 不在往下进行处理
        return Promise.reject(error);
    });