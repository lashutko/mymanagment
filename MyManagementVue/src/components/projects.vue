<template>
   <div class="container">
      <div class="row">
        <section class="col-12">
          <h1>Projects</h1>
          <table class="table table-hover">
              <tr class="header_table">
                  <th>Id</th>
                  <th>Name</th>
                  <th>Group</th>
                  <th>Curator</th>
                  <th>Activities</th>
                  <th>Delete</th>
              </tr>
              <tr v-for="(project, index) in projects">
                  <td>{{project.id}}</td>
                  <td>{{project.name}}</td>
                  <td><router-link class="link" :to="'/group/' + project.group[0].group_id">{{project.group[0].name}}</router-link></td>
                  <td>{{project.curator[0].name}}</td>
                  <td>
                   <!-- <select v-model="activity_id" >    
                      <option value="0" disabled selected>Chose activity</option>   
                      <option v-for="activity in project.activities" :value="activity.activity_id">
                        {{activity.activityDate}} {{activity.hour}}
                      </option>                                            
                    </select> -->
                    <router-link :to="{ name: 'project', params: { project: projects[index] } }">more..</router-link>
                  </td>
                  <td><button class="delete" v-on:click=deleteProject(project.id)>&#x274C;</button></td>
              </tr>
          </table>
        </section>
        <div class="col-12 navigation_buttons">
          <ul>
              <li><router-link class="nav-button" :to="'/projects/add/' + task_id">Add Project</router-link></li>
          </ul>
       </div>
      </div>
    </div>
</template>

<script>

import axios from 'axios';

export default {
  name: 'projects',
  data () {
    return {
      projects: [],
      task_id: this.$route.params.id,
      activity_id: 0
    }
  },
  /*watch: {
    'activity_id': function() {
      this.$router.push('activities/'+ this.activity_id); 
    }
  },*/
  methods: {
      getProjects(){
          axios.get(this.$store.getters.getUrl + 'api/tasks/' + this.task_id)
        .then(function (response) {
            this.projects = response.data.data;
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
        localStorage.setItem('task_id', this.task_id);
      },
      deleteProject(id){
         axios.delete(this.$store.getters.getUrl + 'api/projects/'+ id)
         .then(function (response) {
            alert(response.data['deleted']);
            this.getProjects();
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      }
  },
  created: function () {
    if(!localStorage.getItem('token') || localStorage.getItem('isAdmin') == 1){
      this.$router.push('/login'); 
    }
    this.getProjects();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
