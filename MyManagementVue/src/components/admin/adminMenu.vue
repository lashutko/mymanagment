<template>
   <div class="container">
      <div class="row">
        <section class="col-12">
          <h1>Admin</h1>
          <div class="col-12 navigation_buttons">
            <ul>
                <li><router-link class="nav-button" to="/programmers/">Programmers</router-link></li>
                <li><router-link class="nav-button" to="/curators/">Curators</router-link></li>
            </ul>
          </div>
        </section>
      </div>
    </div>
</template>

<script>

import axios from 'axios';

export default {
  name: 'adminMenu',
  data () {
    return {
      adminMenu: []
    }
  },
  created: function () {
    if(!localStorage.getItem('token') || localStorage.getItem('isAdmin') != 1){
      this.$router.push('/login'); 
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.navigation_buttons ul{
  display: table;
  margin: 0 auto;
}
.navigation_buttons ul li{
  padding: 0 30px;
  float: left;
}
.navigation_buttons ul li a{
  font-size: 24px;

}
</style>
