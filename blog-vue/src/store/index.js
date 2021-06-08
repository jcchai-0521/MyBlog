import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem("token"),
    userInfo: JSON.parse(sessionStorage.getItem("userInfo"))
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
      localStorage.setItem("token", token);
    },
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo;
      sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
    },

    REMOVE_USERINFO: (state) => {
      state.token = "";
      state.userInfo = {};
      localStorage.setItem("token", "");
      sessionStorage.setItem("userInfo", JSON.stringify(""));
    }
  },
  getters:{
    getUserInfo: state => {
      return JSON.parse(sessionStorage.getItem("userInfo"));
    },
    getToken: state => {
      return localStorage.getItem("token");
    }
  },
  actions: {
  },
  modules: {
  }
})
