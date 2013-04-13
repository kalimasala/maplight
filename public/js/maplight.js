$(function() {
  var queryTypeRefine = {
    company: ".refine-company",
    individual: ".refine-individual",
    candidate: ".refine-candidate",
    geographic: ".refine-geographic"
  };

  $("input[name=query-type]").change(function() {
    var $el = $(this);

    $(".refine").children().hide();

    if ($el.is(":checked")) {
      $(queryTypeRefine[$el.val()]).show();
    }
  });

  var candidateNames = [
    "George Bush",
    "John Kerry",
    "Al Gore",
    "Bill Clinton"
  ];
  var split = function( val ) {
    return val.split( /,\s*/ );
  };
  var extractLast = function( term ) {
    return split( term ).pop();
  };
  // Autocomplete multiple, courtesy of http://jqueryui.com/autocomplete/#multiple
  $(".autocomplete-candidate")
    // don't navigate away from the field on tab when selecting an item
    .bind( "keydown", function( event ) {
      if ( event.keyCode === $.ui.keyCode.TAB &&
          $( this ).data( "ui-autocomplete" ).menu.active ) {
        event.preventDefault();
      }
    })
    .autocomplete({
      minLength: 0,
      source: function( request, response ) {
        // delegate back to autocomplete, but extract the last term
        response( $.ui.autocomplete.filter(
          candidateNames, extractLast( request.term ) ) );
      },
      focus: function() {
        // prevent value inserted on focus
        return false;
      },
      select: function( event, ui ) {
        var terms = split( this.value );
        // remove the current input
        terms.pop();
        // add the selected item
        terms.push( ui.item.value );
        // add placeholder to get the comma-and-space at the end
        terms.push( "" );
        this.value = terms.join( ", " );
        return false;
      }
    });
});
