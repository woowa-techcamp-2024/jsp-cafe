String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

function deleteElementById(elementId) {
  var element = document.getElementById(elementId);
  if (element) {
    element.remove();
  } else {
    console.error(`Element with ID ${elementId} not found.`);
  }
}
