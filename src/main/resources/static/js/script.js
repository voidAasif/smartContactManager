console.log("Script working")

const toggleSidebar=()=>{
    if($(".sidebar").is(":visible")){
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    } else {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
};

function deleteContact(contactId){
    console.log("delete Contact Working")
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!"
  }).then((result) => {
    if (result.isConfirmed) {
      window.location="/user/delete/"+contactId;
      Swal.fire({
        title: "Deleted!",
        text: "Your contact has been deleted.",
        icon: "success"
      });
    }
  });
}

const search=()=>{
  console.log("searching..."); //log;

  let query=$("#search-input").val();
  if(query != ''){ //user search something;
    console.log(query);

    //sending request to backend;
    let url = `http://localhost:8123/search/${query}`;

    fetch(url).then(response=>{
      return response.json();
    }).then(data=>{
      console.log(data);

      let text = `<div class='list-group'>`;

      data.forEach((contact)=>{
        text += `<a href='/user/contact/${contact.contactId}' class='list-group-item list-group-item-action'> ${contact.name} </a>`;
      });

      text += `</div>`;

      $(".search-result").html(text);
      $(".search-result").show();
    });

  }else { //user not search anything;
    $(".search-result").hide();
  }
}