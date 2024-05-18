<template>
    <div style="height:8%" class="p-3 mb-2 bg-danger text-white pt-3">
        <h2><Strong>Nearby:</Strong></h2>
    </div>
     <div v-for="hotel in hotelsData" class="mt-4 m-3">
        <button type="button" class="btn btn-outline-danger pt-2"
            @click.prevent="openForm(hotel.id)"
        >
            <h5><strong>{{hotel.name}}</strong></h5>
        </button>
     </div>
</template>

<script>
export default{
    name:'HotelsList',
    props: ['hotelsData', 'openForm']
}

</script>