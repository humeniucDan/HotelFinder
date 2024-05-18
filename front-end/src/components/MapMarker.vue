<template>

</template>

<script>
export default {
  name: 'MapMarker',

  props:['pos', 'id', 'gmap'],

  mounted() {
    console.log("aiic")
    new google.maps.Marker({
      map: gmap,
      position: pos
    });
  }
}
</script>