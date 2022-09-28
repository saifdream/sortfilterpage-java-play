/**
 * Created by saif-dream on 12/17/2015.
 */
(function(angular) {
    'use strict';

    // I act a repository for the remote list collection.
    advanceList.service("advanceListService",function( $http, $q ) {
        // Return public API.
        return({
            listRequest: listRequest,
            getList: getList
        });
        // ---
        // PUBLIC METHODS.
        // ---
        // I post a list with the given data to the remote collection.
        function listRequest( data ) {
            var request = $http.post("/test",data);
            return( request.then( handleSuccess, handleError ) );
        }

        // I get all of the list in the remote collection.
        function getList() {
            var request = $http.get("assets/data/list.json");
            return( request.then( handleSuccess, handleError ) );
        }

        // ---
        // PRIVATE METHODS.
        // ---
        // I transform the error response, unwrapping the application dta from
        // the API response payload.
        function handleError( response ) {
            // The API response from the server should be returned in a
            // nomralized format. However, if the request was not handled by the
            // server (or what not handles properly - ex. server error), then we
            // may have to normalize it on our end, as best we can.
            if (! angular.isObject( response ) || ! response.data.message) {
                return( $q.reject( "An unknown error occurred." ) );
            }
            // Otherwise, use expected error message.
            return( $q.reject( response.data.message ) );
        }
        // I transform the successful response, unwrapping the application data
        // from the API response payload.
        function handleSuccess( response ) {
            return( response.data );
        }
    });

})(window.angular);
