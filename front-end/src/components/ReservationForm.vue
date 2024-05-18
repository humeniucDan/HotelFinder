<template>
    <div id="overlay">
        <div 
          class="p-3 mb-2 bg-danger text-white rounded h-75 position-absolute top-50 start-50 translate-middle"
          style="width:700px"
        >

        <div class="position-absolute top-0 end-0">
          <button type="button" class="btn btn-outline-light"
            @click.prevent="closeForm"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="30" height="35" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
              <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
            </svg>
          </button>
        </div>

        <h2 class="m-5"><strong>{{passedHotelName}}</strong></h2>
        <h3 class="m-5"><strong>Make your reservation</strong></h3>
          <form>
            <div class="form-group m-5">
              <label for="exampleFormControlInput1"><strong>Name:</strong></label>
              <input class="form-control" id="exampleFormControlInput1"
              v-model="curReservation.name"
              >
            </div>
            <div class="form-group m-5">
              <label for="exampleFormControlSelect1"><strong>Room:</strong></label>
              <select class="form-control" id="exampleFormControlSelect1"
                v-model="curReservation.roomNumber"
              >
                <option 
                  v-for="room in rooms"
                >{{"Room: " + room.roomNumber + ", beds: " + room.type + ", price:" + room.price }}</option>
              </select>
            </div>
            <div class="form m-5 d-flex flex-row">
              <div class=" w-50">
                <label for="checkIn"><strong>Check-in:</strong></label>
                <input id="checkIn" class="form-control" type="datetime-local"
                  v-model="curReservation.checkIn"
                >
              </div>
              <div  class=" w-50">
                <label for="checkOut"><strong>Check-out:</strong></label>
                <input id="checkOut" class="form-control" type="datetime-local"
                v-model="curReservation.checkOut"
                >
              </div>
            </div>
            <button type="button" class="btn btn-outline-light m-5"
              @click.prevent="submitForm(curReservation)"
            >Submit reservation</button>
          </form>
        </div>
      </div>
</template>

<script>

export default {
    name:'ReservationForm',
    props: ['passedHotelId', 'passedHotelName', 'submitForm', 'closeForm', 'rooms'],

    data(){
        return {
          curReservation: {hotelId: this.passedHotelId}
        };
    },
}

</script>

<style>
#overlay {
    position: fixed;
    display: block;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0,0,0,0.5);
    z-index: 10;
    cursor: pointer;
}
</style>
