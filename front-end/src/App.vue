<template>
  <form class="form-inline position-absolute top-0 start-50 translate-middle-x" style="z-index: 1">
    <div class="input-group m-2">
      <input class="form-control border border-danger rounded-4 rounded-end" type="number" placeholder="Range" aria-label="Search"
        v-model="range"
      >
      <button class="btn btn-danger rounded-4 rounded-start" type="submit"
        @click.prevent="loadHotelMarkers"
      >Search</button>
    </div>
  </form>
  

  <div class="d-flex justify-content-between flex-direction: column">
    <div id="map-div" style="width:85%; height: 100vh"></div>
    <div class="bg-secondary text-white bg-opacity-25" style="z-index:3; width:15%">
      <hotels-list
        :hotelsData="hotels"
        :openForm="openForm"
      >
      
      </hotels-list>
      <div class="m-3">
        <button type="button" class="btn btn-danger"
          @click.prevent="cancelFormActive = true"
        >
          <h4><strong>Cancel a reservation</strong></h4></button>
      </div>
    </div>
  </div>
 
  <reservation-form
    :closeForm="closeForm" 
    v-if="reservationFormActive == true"
    :passedHotelId="curHotelId"
    :rooms="curHotelRooms"
    :submitForm="requestReservation"
  >
  </reservation-form>

  <cancel-form
    :closeForm="closeCancelForm" 
    v-if="cancelFormActive == true"
  >
  </cancel-form>

</template>

<style>
  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
    -webkit-appearance: none;
  }
</style>

<script>
import { Loader } from '@googlemaps/js-api-loader';
// import myApiKey  from "./api.key"
import HotelsList from './components/ValidHotelsList.vue';
import ReservationForm from './components/ReservationForm.vue';
import CancelForm from './components/CancelForm.vue';
// import split from 'core-js/fn/symbol/split';


export default {
  components:{
    HotelsList,
    ReservationForm,
    CancelForm
  },

  data(){
    return {
      lat: 0,
      lng: 0,
      range: null,
      position: new Object(),
      map: [],
      hotels: [],
      curPosMarker: [],
      hotelMarkers: [],
      reservationFormActive: false,
      curHotelId: 0,
      curHotelRooms: [],
      cancelFormActive: false
    };
  },

  created() {
    this.getLocation()
    this.loadMap()
    // this.loadHotelMarkers()
  },

  methods: {
    getLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          this.lat = position.coords.latitude   
          this.lng = position.coords.longitude
          this.position.lat = position.coords.latitude
          this.position.lng = position.coords.longitude
          // console.log("getloc " + this.lat)
        })
      } else {
        console.log("Geolocation is not supported by this browser.")
      }
    },

    async loadMap() {

      var myApiKey = await fetch("lo/api.key")

      console.log(myApiKey.json)
  
      const loader = new Loader({ apiKey: });

      try {
          await loader.importLibrary("maps");

          const position = { lat: this.lat, lng: this.lng};

          this.map = new google.maps.Map(document.getElementById("map-div"), {
              zoom: 15,
              center: {lat: this.lat, lng: this.lng},
              // mapId: "NoPOI"
              styles:  [
                      {
                        featureType: "poi",
                        stylers: [{ visibility: "off" }],
                      }
                    ]
          });

          this.curPosMarker = new google.maps.Marker({
            map: this.map,
            position: {lat: this.lat, lng: this.lng},
            icon: {
              url:'https://maps.google.com/mapfiles/ms/icons/blue-dot.png',
              labelOrigin: {
                x: 15,
                y: 40
              }
            },
            label: { color: '#000000', fontWeight: 'bold', fontSize: '14px', text: 'You are here' }
          });


      } catch (error) {
          console.error("Error loading map:", error);
          
      }
    },

    async openForm(hotelId){
      let roomsData = await fetch("http://localhost:8000/hotels/retRooms-" + hotelId)
      let tempRooms = await roomsData.json()
      this.curHotelRooms = tempRooms
      console.log(hotelId, tempRooms)
      this.reservationFormActive = true
      this.curHotelId = hotelId
      console.log(this.curHotelId)
    },

    closeForm(){
      this.reservationFormActive = false;
      console.log(this.reservationFormActive)
    },

    async requestReservation(reservation){
      if(!reservation.name || reservation.roomNumber == undefined || !reservation.checkIn || !reservation.checkOut) {
        alert("Please fill the whole form!")
        return
      }

      console.log(reservation, reservation.roomNumber)
      var splitData = reservation.roomNumber.split(' ')
      reservation.roomNumber = splitData[1].slice(0, -1)

      console.log(reservation);
      let response = await fetch("http://localhost:8000/reservations", {
          method: "POST",
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(reservation)
      })
      var respData = await response.json()

      if(!respData) {
        alert("Selected room is not available at that time.")
        return
      }

      console.log(respData)
    },

    async loadHotelMarkers(){

      for(var mi in this.hotelMarkers){
        console.log(this.hotelMarkers[mi])  
        this.hotelMarkers[mi].setMap(null)
        this.hotelMarkers[mi].setVisible(false)
      }

      this.hotelMarkers = []
      
      let hotelsJSON = await fetch("http://localhost:8000/hotels/retall", {
          method: "POST",
          headers: { 'Content-Type': 'application/json' },
          body: "{\"range\":\"" + this.range + "\", \"lat\":\"" + this.lat + "\", \"lng\":\"" + this.lng + "\"}"
      })
      this.hotels = await hotelsJSON.json()

      for(var i in this.hotels){
        let hotel  = this.hotels[i]
        var curHotelMarker = new google.maps.Marker({
              map: this.map,
              position: {lat: hotel.lat, lng: hotel.lng},
              title: hotel.name,
              icon: {
                url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png',
                labelOrigin: {
                  x: 15,
                  y: 40
                }
              },
              label: { color: '#000000', fontWeight: 'bold', fontSize: '14px', text: hotel.name }
        })
        this.hotelMarkers.push(curHotelMarker);

        google.maps.event.addListener(curHotelMarker, 'click', () => { 
            var localHotelId = hotel.id
            console.log(hotel.id)
            this.openForm(localHotelId);
        });

        console.log(this.hotelMarkers[this.hotelMarkers.length-1])
      }
    },
    closeCancelForm(){
      this.cancelFormActive = false;
    }
  }
}
</script>
