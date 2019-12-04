import Vue from 'vue'
import Router from 'vue-router'
import login from '@/components/login'
import projects from '@/components/projects'
import project from '@/components/project'
import addProgrammerAttandance from '@/components/addProgrammerAttandance'
import editProject from '@/components/editProject'
import tasks from '@/components/tasks'
import groups from '@/components/groups'
import group from '@/components/group'
import activity from '@/components/activity'
import addGroup from '@/components/addGroup'
import editGroup from '@/components/editGroup'
import addProject from '@/components/addProject'
import addTask from '@/components/addTask'
import editTask from '@/components/editTask'
import addActivity from '@/components/addActivity'
import CalendarShow from '@/components/CalendarShow'
import Attandancelist from '@/components/attandanceList'
import totalAttandance from '@/components/totalAttandance'
import adminMenu from '@/components/admin/adminMenu'
import adminProgrammers from '@/components/admin/adminProgrammers'
import adminCurators from '@/components/admin/adminCurators'
import addCurator from '@/components/admin/addCurator'
import addAdmin from '@/components/admin/addAdmin'
import addProgrammer from '@/components/admin/addProgrammer'
import addProgrammerGroup from '@/components/addProgrammerGroup'
import editCurator from '@/components/admin/editCurator'
import editProgrammer from '@/components/admin/editProgrammer'

Vue.use(Router)

export default new Router({
    mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: projects
    },
    {
      path: '/programmers/add',
      name: 'addProgrammerGroup',
      component: addProgrammerGroup
    },
    {
      path: '/projects/add/:id',
      name: 'addProject',
      component: addProject
    },
    {
      path: '/groups/add',
      name: 'addGroup',
      component: addGroup
    },
    {
      path: '/projects/:id',
      name: 'projects',
      component: projects
    },
    {
      path: '/project/',
      name: 'project',
      props: true,
      component: project
    },
    {
      path: '/addProgrammerAttandance/',
      name: 'addProgrammerAttandance',
      props: true,
      component: addProgrammerAttandance
    },
    {
      path: '/project/edit/:id',
      name: 'editProject',
      props: true,
      component: editProject
    },
    {
      path: '/tasks',
      name: 'tasks',
      component: tasks
    },
    {
      path: '/task/edit/:id',
      name: 'editTask',
      props: true,
      component: editTask
    },
    {
      path: '/tasks/add',
      name: 'addTask',
      component: addTask
    },
    {
      path: '/groups',
      name: 'groups',
      component: groups
    },
    {
      path: '/group/:id',
      name: 'group',
      component: group
    },
    {
      path: '/group/edit/:id',
      name: 'editGroup',
      props: true,
      component: editGroup
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/activities/:id',
      name: 'activity',
      component: activity
    },
    {
      path: '/activity/add',
      name: 'addActivity',
      component: addActivity
    },
    {
      path: '/attandancelist/add',
      name: 'Attandancelist',
      component: Attandancelist
    },
    {
      path: '/activity/show',
      name: 'CalendarShow',
      component: CalendarShow
    },
    {
      path: '/admin',
      name: 'adminMenu',
      component: adminMenu
    },
    {
      path: '/programmers',
      name: 'adminProgrammers',
      component: adminProgrammers
    },
    {
      path: '/curators',
      name: 'adminCurators',
      component: adminCurators
    },
    {
      path: '/curator/add',
      name: 'addCurator',
      component: addCurator
    },
    {
      path: '/admin/add',
      name: 'addAdmin',
      component: addAdmin
    },
    {
      path: '/curator/edit/:id',
      name: 'editCurator',
      component: editCurator
    },
    {
      path: '/programmer/add',
      name: 'addProgrammer',
      component: addProgrammer
    },
    {
      path: '/programmer/edit/:id',
      name: 'editProgrammer',
      component: editProgrammer
    },
    {
      path: '/attandancelist/:id',
      name: 'totalAttandance',
      component: totalAttandance
    }
  ]
})
