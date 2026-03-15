function likePost(id) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", `/posts/${id}/like`, true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    // Setting the displayed like count to match the one obtained from the database
    xhr.onload = function () {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            document.getElementById(`like-count-${id}`).textContent = response.likeCount;
        }
    };

    xhr.send(JSON.stringify({post_id: id}));
}