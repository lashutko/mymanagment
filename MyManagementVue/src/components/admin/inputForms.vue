<template>
  <div class="row" style="margin-top: 15px;"> 
  	<div class="col-4">
  		<input v-model="programmer.name" type="text" placeholder="Programmer name and surename" id="name"/>
  	</div>
  	<div class="col-4">
  		<input v-model="programmer.email" type="email" placeholder="Programmer email" id="email"/> 
  	</div>
  	<div class="col-4">
  		<input v-model="programmer.tagId" type="text" placeholder="Programmer tag id" id="tagId"/> 
  	</div>
  </div>
</template>

<script>
  export default {
	name: 'inputForms',
	props: ['input'],
	data() {
      return {
        programmer:  {
        	name: '',
        	email: '',
        	tagId: '',
        	id: null
      	}
      }
    },
    methods: {

    },
    watch: {
  	'programmer.name': function() {
  		this.programmer.id = this.input;
      	this.$emit('save', this.programmer);
  
  	  }
	},
}
</script>
<style scoped>
button.primary {
background: blue;
color: white;
}
</style>