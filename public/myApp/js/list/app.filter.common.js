/**
 * Created by saif-dream on 10/3/2016.
 */
(function(angular) {
    'use strict';
    angular.module('advanceList',[
        'ngAnimate',
        'ngMaterial',
        'ngMessages',
        'ui.bootstrap',
        'gm.datepickerMultiSelect',
        'ngTagsInput',
        'elif',
        'angularjs-dropdown-multiselect',
        'ngComboDatePicker',
        'bw.paging',
        'nya.bootstrap.select'
    ])
        .constant('maxSize', 5)
        .constant('initialRowsPerPage', 10)
        .value('rows', [5,10,15,20,25,30])
        .value('commonFilterList', [
            {id: 1, label: 'Equals'},
            {id: 2, label: 'Not Equals'},
            {id: 15, label: 'In'},
            {id: 16, label: 'Not In'}
        ])
        .value('stringSpFilterList', [
            {id: 3, label: 'Starts with'},
            {id: 4, label: 'Not Starts with'},
            {id: 5, label: 'End with'},
            {id: 6, label: 'Not End with'},
            {id: 7, label: 'Contains'},
            {id: 8, label: 'Not Contains'},
            {id: 19, label: 'Sounds Like'}
        ])
        .value('numberSpFilterList', [
            {id: 9, label: 'Less than'},
            {id: 10, label: 'Less than eq'},
            {id: 11, label: 'Greater than'},
            {id: 12, label: 'Greater than eq'},
            {id: 13, label: 'Between'},
            {id: 14, label: 'Not Between'}
        ])
        .value('nullSpFilterList', [
            {id: 17, label: 'Null'},
            {id: 18, label: 'Not Null'}
        ])
        .factory('PageProperties', function() {
            function PageProperties(currentPage, rowsPerPage, totalPage, totalRows, displayCounter) {
                this.currentPage = currentPage;
                this.rowsPerPage = rowsPerPage;
                this.totalPage = totalPage;
                this.totalRows = totalRows;
                this.displayCounter = displayCounter;
            }
            PageProperties.prototype.setPage = function(currentPage, totalPage, totalRows, displayCounter) {
                this.currentPage = currentPage;
                this.totalPage = totalPage;
                this.totalRows = totalRows;
                this.displayCounter = displayCounter;
            }
            PageProperties.prototype.setCurrentPage = function(currentPage) {
                this.currentPage = currentPage;
            }
            PageProperties.prototype.getJson = function() {
                var jsonData = {
                    "currentPage":this.currentPage,
                    "rowsPerPage":this.rowsPerPage
                };
                return jsonData;
            }
            return PageProperties;
        })
        .factory('ColumnProperties', function() {
            function ColumnProperties(sortColumnName, isAscending) {
                this.sortColumnName = sortColumnName;
                this.isAscending = isAscending;
            }
            ColumnProperties.prototype.setColumn = function(sortColumnName, isAscending){
                this.sortColumnName = sortColumnName;
                this.isAscending = isAscending;
            }
            ColumnProperties.prototype.getJson = function() {
                var jsonData = {
                    "sortColumnName":this.sortColumnName,
                    "isAscending":this.isAscending
                };
                return jsonData;
            }
            ColumnProperties.prototype.setColumnValue = function(columnName){
                if(this.sortColumnName == columnName){
                    if(this.isAscending == true){
                        this.isAscending = false;

                    } else {
                        this.isAscending = true;
                    }
                } else {
                    this.isAscending = true;
                }
                this.sortColumnName = columnName;
            }
            return ColumnProperties;
        })
        .factory('citySharedService', function($rootScope) {
            var citySharedService = {};
            citySharedService.city = '';
            citySharedService.prepForBroadcast = function(city) {
                this.city = city;
                this.broadcastItem();
            };
            citySharedService.broadcastItem = function() {
                $rootScope.$broadcast('handleCityBroadcast');
            };
            return citySharedService;
        })
        .factory('roleSharedService', function($rootScope) {
            var roleSharedService = {};
            roleSharedService.role = '';
            roleSharedService.prepForBroadcast = function(role) {
                this.role = role;
                this.broadcastItem();
            };
            roleSharedService.broadcastItem = function() {
                $rootScope.$broadcast('handleRoleBroadcast');
            };
            return roleSharedService;
        })
        .factory('addressSharedService', function($rootScope) {
            var addressSharedService = {};
            addressSharedService.address = '';
            addressSharedService.prepForBroadcast = function(address) {
                this.address = address;
                this.broadcastItem();
            }
            addressSharedService.broadcastItem = function() {
                $rootScope.$broadcast('handleAddressBroadcast');
            }
            return addressSharedService;
        })
        .service('stringToArray',function(){
            function getStringArray(fullString, separator){
                var fullArray = [];
                if (fullString !== undefined) {
                    fullString = fullString.replace(/\s/g, "")
                    if (fullString.indexOf(separator) == -1) { fullArray.push(fullString); }
                    else { fullArray = fullString.split(separator); }
                }
                return fullArray;
            }
            return({ getStringArray: getStringArray });
        })
        .service('FilterTypeService', function(CommonFilterService, NullFilterService, SpStringFilterService, UsernameFilterService,
                                               NumberFilterService, DateFilterService){
            function getFilterId(type, value){
                console.log(type, value)
                var id;
                switch (type) {
                    case "Username":
                        if(id = CommonFilterService.getFilterId(value) > 0){
                            id = CommonFilterService.getFilterId(value);
                        } else if(id = UsernameFilterService.getFilterId(value[0]) > 0){
                            id = UsernameFilterService.getFilterId(value[0]);
                        } else id = 0;
                        break;

                    case "String":
                        if(id = NullFilterService.getFilterId(value[0]) > 0){
                            id = NullFilterService.getFilterId(value[0]);
                        } else if(id = CommonFilterService.getFilterId(value) > 0){
                            id = CommonFilterService.getFilterId(value);
                        } else if(id = SpStringFilterService.getFilterId(value[0]) > 0){
                            id = SpStringFilterService.getFilterId(value[0]);
                        } else id = 0;
                        break;

                    case "Date":
                        if(id = NullFilterService.getFilterId(value[0]) > 0){
                            id = NullFilterService.getFilterId(value[0]);
                        } else if(id = CommonFilterService.getFilterId(value) > 0){
                            id = CommonFilterService.getFilterId(value);
                        } else if(id = DateFilterService.getFilterId(value[0]) > 0){
                            id = DateFilterService.getFilterId(value[0]);
                        } else id = 0;
                        break;

                    case "Number":
                        if(id = NullFilterService.getFilterId(value[0]) > 0){
                            id = NullFilterService.getFilterId(value[0]);
                        } else if(id = CommonFilterService.getFilterId(value) > 0){
                            id = CommonFilterService.getFilterId(value);
                        } else if(id = NumberFilterService.getFilterId(value[0]) > 0){
                            id = NumberFilterService.getFilterId(value[0]);
                        } else id = 0;
                        break;
                }
                /*var typeCheck = value.replace(/[^\w\s]/gi, '');
                console.log(typeCheck)
                if (typeCheck.match(/^[a-zA-Z0-9]+([-_\.][a-zA-Z0-9]+)*[a-zA-Z0-9]$/g)){
                    console.log("It's a Username")
                }
                if (typeCheck.search(/[^a-zA-Z]+/) === -1){
                    console.log("It's a String value")
                }
                if (typeCheck.match(/^\d+$/)){
                    console.log("It's a Number value")
                }*/
                /*if (value.match(/\d{4}-\d{1,2}-\d{1,2}/ig)){
                    console.log("It's a Date value")
                }*/
                return id;
            }
            return ({getFilterId: getFilterId});
        })
        .service('CommonFilterService', function(){
            function getFilterId(value){
                /*if (value.match(/(=)([A-Z])\w+/ig) || value.match(/^=+[0-9]+$/)){
                 return 1;
                 } else if (value.match(/(!)([A-Z])\w+/ig) || value.match(/^!+[0-9]+$/)) {
                 return 2;
                 } else */
                if(value.length > 1){
                    if(angular.equals(value[0].charAt(0), "!")){
                        return 16;
                    } else return 15;
                } else return 0;
            }
            return ({getFilterId: getFilterId})
        })
        .service('NullFilterService', function(){
            function getFilterId(value){
                if ( angular.equals(value, "Null") || angular.equals(value, "null") ) {
                    return 17;
                } else if ( angular.equals(value, "!Null") || angular.equals(value, "!null") ) {
                    return 18;
                } else return 0;
            }
            return ({getFilterId: getFilterId})
        })
        .service('SpStringFilterService', function(){
            function getFilterId(value) {
                if (value.match(/^[!][%].*[%]$/ig)){
                    return 8;
                } else if (value.match(/^[%].*[%]$/ig)){
                    return 7;
                } else if (value.match(/(!%)([A-Z])\w+/ig)){ //a-zA-Z0-9
                    return 6;
                } else if (value.match(/(%)([A-Z])\w+/ig)){
                    return 3;
                } else if (value.match(/^[!].*[%]$/ig)){
                    return 4;
                } else if (value.match(/^.*[%]$/ig)){
                    return 5;
                } else if (value.match(/(!)([A-Z])\w+/ig)) {
                    return 2;
                } else if (value.match(/([A-Z])\w+/ig)){
                    return 1;
                } else return 0;
            }
            return ({getFilterId:getFilterId});
        })
        .service('UsernameFilterService', function(){
            function getFilterId(value){
                if (value.match(/^!+%+[a-zA-Z0-9.\-_$@*!]+%$/)){ ///^[a-zA-Z0-9.\-_$@*!]{1,10}$/
                    return 8;
                } else if (value.match(/^%+[a-zA-Z0-9.\-_$@*!]+%$/)){
                    return 7;
                } else if (value.match(/^!+%+[a-zA-Z0-9.\-_$@*!]+$/)){
                    return 6;
                } else if (value.match(/^%+[a-zA-Z0-9.\-_$@*!]+$/)){
                    return 3;
                } else if (value.match(/^!+[a-zA-Z0-9.\-_$@*!]+%$/)){
                    return 4;
                } else if (value.match(/^[a-zA-Z0-9.\-_$@*!]+%$/)){
                    return 5;
                } else if (value.match(/^!+[a-zA-Z0-9.\-_$@*!]+$/)) {
                    return 2;
                } else if (value.match(/^[a-zA-Z0-9.\-_$@*!]+$/)){
                    return 1;
                } else return 0;
            }
            return ({getFilterId: getFilterId})
        })
        .service('NumberFilterService', function(){
            function getFilterId(value){
                if (value.match(/^[0-9]+$/)){
                    return 1;
                } else if (value.match(/^!+[0-9]+$/)) {
                    return 2;
                } else if (value.match(/^<+[0-9]+$/) || value.match(/^[0-9]+>+$/)) {
                    return 9;
                } else if (value.match(/^<+=+[0-9]+$/) || value.match(/^[0-9]+=+>+$/)) {
                    return 10;
                } else if (value.match(/^>+[0-9]+$/) || value.match(/^[0-9]+<+$/)) {
                    return 11;
                } else if (value.match(/^>+=+[0-9]+$/) || value.match(/^[0-9]+=+<+$/)) {
                    return 12;
                } else if (value.match(/^[0-9]+-[0-9]+$/)) {
                    return 13;
                } else if (value.match(/^!+[0-9]+-[0-9]+$/)) {
                    return 14;
                } else return 0;
            }
            return ({getFilterId: getFilterId})
        })
        .service('DateFilterService', function(){
            function getFilterId(value){
                if (value.match(/^\d{4}-\d{1,2}-\d{1,2}$/)){
                    return 1;
                } else if (value.match(/^!+\d{4}-\d{1,2}-\d{1,2}$/)) {
                    return 2;
                } else if (value.match(/^<+\d{4}-\d{1,2}-\d{1,2}$/) || value.match(/^\d{4}-\d{1,2}-\d{1,2}>$/)) {
                    return 9;
                } else if (value.match(/^<=+\d{4}-\d{1,2}-\d{1,2}$/) || value.match(/^\d{4}-\d{1,2}-\d{1,2}=>$/)) {
                    return 10;
                } else if (value.match(/^>+\d{4}-\d{1,2}-\d{1,2}$/) || value.match(/^\d{4}-\d{1,2}-\d{1,2}<$/)) {
                    return 11;
                } else if (value.match(/^>=+\d{4}-\d{1,2}-\d{1,2}$/) || value.match(/^\d{4}-\d{1,2}-\d{1,2}=<$/)) {
                    return 12;
                } else return 0;
            }
            return ({getFilterId: getFilterId})
        })
        .service( 'advanceListService', function($http, $q) {
            // I post a list with the given data to the remote collection.
            function listRequest(url, data) {
                // The timeout property of the http request takes a deferred value
                // that will abort the underying AJAX request if / when the deferred
                // value is resolved.
                var deferredAbort = $q.defer();
                var request = $http({ method: "post", url: url, data: data, timeout: deferredAbort.promise });

                // Rather than returning the http-promise object, we want to pipe it
                // through another promise so that we can "unwrap" the response
                // without letting the http-transport mechansim leak out of the
                // service layer.
                var promise = request.then( handleSuccess, handleError );

                // Now that we have the promise that we're going to return to the
                // calling context, let's augment it with the abort method. Since
                // the $http service uses a deferred value for the timeout, then
                // all we have to do here is resolve the value and AngularJS will
                // abort the underlying AJAX request.
                promise.abort = function() { deferredAbort.resolve(); };

                // Since we're creating functions and passing them out of scope,
                // we're creating object references that may be hard to garbage
                // collect. As such, we can perform some clean-up once we know
                // that the requests has finished.
                promise.finally(
                    function() {
                        console.info( "Cleaning up object references." );
                        promise.abort = angular.noop;
                        deferredAbort = request = promise = null;
                    }
                );
                return( promise );
            }
            // I transform the error response, unwrapping the application dta from
            // the API response payload.
            function handleError( response ) {
                // The API response from the server should be returned in a
                // nomralized format. However, if the request was not handled by the
                // server (or what not handles properly - ex. server error), then we
                // may have to normalize it on our end, as best we can.
                if (! angular.isObject( response ) || ! response.data || ! response.data.message) {
                    if(angular.equals(response.status, -1)){
                        return( $q.reject( "Request Aborted" ) );
                    }
                    return( $q.reject( response.statusText + " ( Error code : "+response.status+")" ) );
                }
                // Otherwise, use expected error message.
                return( $q.reject( response.data.message ) );
            }
            // I transform the successful response, unwrapping the application data
            // from the API response payload.
            function handleSuccess( response ) { return( response.data ); }
            // Return the public API.
            return({ listRequest: listRequest });
        });
})(window.angular);