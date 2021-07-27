import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router';
import Home from '../views/Home.vue';
import AdminUser from '../views/admin/AdminUser.vue';
import AdminEbook from '../views/admin/AdminEbook.vue';
import AdminCategory from '../views/admin/AdminCategory.vue';

import AdminDoc from '../views/admin/AdminDoc.vue';
import {Tool} from "@/util/tool";
import store from "@/store";


const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // component: About
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/chart',
    name: 'Chart',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/Chart.vue')
  },
  {
    path: '/pie',
    name: 'Pie',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/Pie.vue')
  },
  {
    path: '/flow',
    name: 'Flow',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/Flow.vue')
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: AdminUser,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: AdminEbook,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: AdminCategory,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: AdminDoc,
    meta: {
      loginRequire: true
    }
  }, {
    path: '/admin/Demo',
    name: 'Demo',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/Demo.vue')
  },
  {
    path: '/md/simple/demo',
    name: 'SimpleMDE',
    component: () => import(/* webpackChunkName: "about" */ '../views/md/SimpleMDEDemo.vue')
  },
  {
    path: '/md/stack/demo',
    name: 'StackEdit',
    component: () => import(/* webpackChunkName: "about" */ '../views/md/MaveonEditor.vue')
  },
  {
    path: '/doc',
    name: 'doc',
    component: () => import(/* webpackChunkName: "about" */ '../views/Doc.vue')
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      console.log("用户未登录！");
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
