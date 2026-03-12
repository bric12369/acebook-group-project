function likePost(id) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", `/posts/${id}/like`, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({post_id: id}));
}