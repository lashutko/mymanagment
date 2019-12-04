<template>
   <div class="container">
      <div class="row" v-if="curators">
        <section class="col-12">
          <h1>Curators</h1>
          <table class="table table-hover">
              <tr class="header_table">
                  <th>Id</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Tag id</th>
                  <th>Edit</th>
                  <th>Delete</th>
              </tr>
              <tr v-for="curator in curators">
                  <td>{{curator.id}}</td>
                  <td>{{curator.name}}</td>
                  <td>{{curator.email}}</td>
                  <td>{{curator.tagId}}</td>
                  <td><router-link class="nav-button" :to="'/curator/edit/' + curator.id">Edit curator</router-link></td>
                  <td><button class="delete" v-on:click=deleteCurator(curator.id)>&#x274C;</button></td>
              </tr>
          </table>
        </section>
        <div class="col-12 navigation_buttons">
          <ul>
              <li><router-link class="nav-button" to="/curator/add">Add Task</router-link></li>
          </ul>
       </div>
      </div>
    </div>
</template>

<script>

import axios from 'axios';

export default {
  name: 'tasks',
  data () {
    return {
      curators: []
    }
  },
  methods: {
      getCurators(){
          axios.get(this.$store.getters.getUrl + 'api/curators')
        .then(function (response) {
            this.curators = response.data;
            if(response.data =="unauthorized"){
              this.$router.push('/login'); 
            }
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      },
      deleteCurator(id){
         axios.delete(this.$store.getters.getUrl + 'api/curators/'+ id)
         .then(function (response) {
            alert(response.data['deleted']);
            this.getCurators();
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      }
  },
  created: function () {
    if(!localStorage.getItem('token')){
      this.$router.push('/login'); 
    }
    this.getCurators();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
