/* List Page Application Using AngularJS */
var listApp = angular.module('listApp', ['ngAnimate','ui.bootstrap','elif','bw.paging']);

listApp.directive("ngFormCommit", [function(){
	return {
		require:"form",
		link: function($scope, $el, $attr, $form) {
			$form.commit = function() {
				$el[0].submit();
			};
		}
	};
}])

/* List Page Controller */
listApp.controller('ListController', function ($scope, $compile, $log) {

	$scope.getCol = function(col){
		var colId = col+"Col";
		document.getElementById(colId).className = "selectedcolumn";
		
		document.getElementById(col).classList.remove('sort-btn');
	};
	
	/* Change isMarried check box value */
	$scope.isMarried = function(value){
		if(value == false){
			$scope.isMarried = false;
		}else {
			$scope.isMarried = true;
		}
    };
    
	$scope.maxSize = 5;
	$scope.bigTotalItems = document.getElementById("total").value;
	
	$scope.bigCurrentPage = document.getElementById("currentPage").value;
	//$log.log( "Current Page is : " + $scope.bigCurrentPage );
	
	$scope.itemsPerPage = document.getElementById("rowsCount").value;
	
	/* First commented pagination function */
	/*
	$scope.pageChanged = function(page) {
	    document.getElementById("currentPage").value = page;
	    document.getList.submit();
	};*/
	
	/* Second pagination function */
	
	/* A function to do some act on paging click
	   In reality this could be calling a service which 
	   returns the items of interest from the server
	   based on the page parameter
     */
	$scope.doCtrlPagingAct = function(text, page, pageSize, total){
		//$log.log({text, page, pageSize, total});
        document.getElementById("currentPage").value = page;
	    document.getList.submit();
    };
    
    /* Filter function */
    $scope.getFilterPage = function(){
    	document.getElementById("currentPage").value = 1;
		document.getList.submit();
    };
    

	$scope.updated = 0;
	$scope.$watch('sortColumnName', function(newValue, oldValue) {
		if (newValue === oldValue) { return; }
		$scope.updated++;
	    $log.log( "Sorted column change : " + $scope.updated + " times." );
	}, true);

    
    
    /* Column Sorting Function */
	$scope.setColumnValue = function(name){
		
		if($scope.sortColumnName == name){
			//document.getElementById("ID").className = "selectedcolumn";
			if($scope.isAscending == true){
				document.getElementById("isAscending").value = false;
				$scope.isAscending = false;
				
			} else {
				document.getElementById("isAscending").value = true;
				$scope.isAscending = true;
			}	
		} else {
			document.getElementById("isAscending").value = true;
			$scope.isAscending = true;
		}
		
		document.getElementById("sortColumnName").value = name;
	    $scope.sortColumnName = name;
		document.getList.submit();
	}

	/* Submit function */
	$scope.submit = function(page, $form){
		document.getElementById("currentPage").value = page;
		document.getList.submit();
		//$scope.currentPage = page;
		//$form.commit();
	}
	
	/* Cursor positioning function */
	//$scope.caretPos = 1;

    $scope.setSelectionRange = function(input, selectionStart, selectionEnd) {
	    if (input.setSelectionRange) {
	      input.focus();
	      input.setSelectionRange(selectionStart, selectionEnd);
	      
	    } else if (input.createTextRange) {
	      var range = input.createTextRange();
	      range.collapse(true);
	      range.moveEnd('character', selectionEnd);
	      range.moveStart('character', selectionStart);
	      range.select();
	    }
	};

	$scope.setCaretToPos = function(id, str, pos) {
		document.getElementById(id).value = str;
		$scope.setSelectionRange(document.getElementById(id), pos, pos);
		/*
		if(value == "contain"){
			document.getElementById(id).value = "%%";
		    $scope.setSelectionRange(document.getElementById(id), 1, 1);
		} else if ( value == "start"){
			document.getElementById(id).value = "%";
		    $scope.setSelectionRange(document.getElementById(id), 0, 0);
		} else if ( value == "end") {
			document.getElementById(id).value = "%";
		    $scope.setSelectionRange(document.getElementById(id), 1, 1);
		} else if ( value == "getter") {
			document.getElementById(id).value = "<";
			$scope.setSelectionRange(document.getElementById(id), 0, 0);
		} else if ( value == "getterEq") {
			document.getElementById(id).value = "=<";
			$scope.setSelectionRange(document.getElementById(id), 0, 0);
		} else if ( value == "less") {
			document.getElementById(id).value = "<";
			$scope.setSelectionRange(document.getElementById(id), 1, 1);
		} else if ( value == "lessEq") {
			document.getElementById(id).value = "<=";
			$scope.setSelectionRange(document.getElementById(id), 2, 2);
		} else if ( value == "in") {
			document.getElementById(id).value = "IN()";
			$scope.setSelectionRange(document.getElementById(id), 3, 3);
		} else if ( value == "notIn") {
			document.getElementById(id).value = "NOT IN()";
			$scope.setSelectionRange(document.getElementById(id), 7, 7);
		} else if ( value == "null") {
			document.getElementById(id).value = "NULL";
			$scope.setSelectionRange(document.getElementById(id), 5, 5);
		} else if ( value == "notNull") {
			document.getElementById(id).value = "NOT NULL";
			$scope.setSelectionRange(document.getElementById(id), 8, 8);
		} else if ( value == "sLike") {
			document.getElementById(id).value = "sLike()";
			$scope.setSelectionRange(document.getElementById(id), 6, 6);
		} else {}
		 */
	    //$log.log( "I am in setCaretToPos value is : " + document.getElementById("usernameF").value );
	};
	
	/* Date range picker */
	
	// Disable weekend selection
	$scope.disabled = function(date, mode) {
	    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
	};

	$scope.toggleMin = function() {
	    $scope.minDate = $scope.minDate ? null : new Date();
	};
	//$scope.toggleMin();
	
	$scope.maxDate = new Date(2050, 5, 22);
	
	$scope.dateFrom = function($event) {
	    $scope.dateFrom.openFrom = true;
	};
	  
	$scope.dateTo = function($event) {
		$scope.toggleMin();
		$scope.dateTo.opendateTo = true;
	};

	$scope.dateOptions = {
	    formatYear: 'yy',
	    startingDay: 1
	};

	$scope.formats = ['yyyy-MM-dd  HH:mm','dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
		$scope.format = $scope.formats[0];

	$scope.status = {
	    opened: false
	};

	var tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);
	
	var afterTomorrow = new Date();
	afterTomorrow.setDate(tomorrow.getDate() + 2);
	$scope.events =
	    [
	      {
	        date: tomorrow,
	        status: 'full'
	      },
	      {
	        date: afterTomorrow,
	        status: 'partially'
	      }
	    ];

	$scope.getDayClass = function(date, mode) {
	    if (mode === 'day') {
	    	var dayToCheck = new Date(date).setHours(0,0,0,0);

	      for (var i=0;i<$scope.events.length;i++){
	        var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

	        if (dayToCheck === currentDay) {
	          return $scope.events[i].status;
	        }
	      }
	    }

	    return '';
	};
    
});
