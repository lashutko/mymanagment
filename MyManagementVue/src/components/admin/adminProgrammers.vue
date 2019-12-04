<template>
   <div class="container">
      <div class="row" v-if="programmers">
        <section class="col-12">
          <h1>Programmers</h1>
          <table class="table table-hover">
              <tr class="header_table">
                  <th>Id</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>TagId</th>
                  <th>Edit</th>
                  <th>Delete</th>
              </tr>
              <tr v-for="programmer in programmers">
                  <td>{{programmer.programmer_id}}</td>
                  <td>{{programmer.name}}</td>
                  <td>{{programmer.email}}</td>
                  <td>{{programmer.tagId}}</td>
                  <td><router-link class="nav-button" :to="'/programmer/edit/' + programmer.programmer_id">Edit</router-link></td>
                  <td><button class="delete" v-on:click=deleteProgrammer(programmer.programmer_id)>&#x274C;</button></td>
              </tr>
          </table>
        </section>
        <div class="col-12 navigation_buttons">
          <ul>
              <li><router-link class="nav-button" to="/programmer/add">Add Programmer</router-link></li>
          </ul>
       </div>
      </div>
    </div>
</template>

<script>

import axios from 'axios';

export default {
  name: 'adminProgrammer',
  data () {
    return {
      programmers: []
    }
  },
  methods: {
      getProgrammers(){
          axios.get(this.$store.getters.getUrl + 'api/programmers')
        .then(function (response) {
            this.programmers = response.data;
            if(response.data =="unauthorized"){
              this.$router.push('/login'); 
            }
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      },
      deleteProgrammer(id){
         axios.delete(this.$store.getters.getUrl + 'api/programmers/'+ id)
         .then(function (response) {
            alert(response.data['deleted']);
            this.getProgrammers();
        }.bind(this))
        .catch((error)=>{
          this.$router.push('/login'); 
        });
      }
  },
  created: function () {
    if(!localStorage.getItem('token') || localStorage.getItem('isAdmin') != 1){
      this.$router.push('/login'); 
    }
    this.getProgrammers();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
