function drag_upload(){
	/*var voiceId;
	var progressBar;
	function createVoiceMeta() {
		var url = "/voices/add";
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if(this.readyState == 4 && this.status == 200) {
				console.log(this.responseText);
				voiceId = this.responseText;
			}
		};
		xhttp.open("POST", url, true);
		xhttp.setRequestHeader("Content-type", "text/plain");
		xhttp.send();
	}
	createVoiceMeta();
		var fileselect = $("#uploadFile");
		var filedrag = $("#fileArea");
		
		
		progressBar = new ProgressBar.Line('.bar_percent', {
			 strokeWidth: 8,
			  easing: 'easeInOut',
			  duration: 5000,
			  color: '#0091d6',
			  trailColor: '#eee',
			  trailWidth: 8,
			  step: function(state, bar, attachment) {
				  $("#percent").text(Math.ceil(100.0 - state.offset));
			  }
		});
		
		// file drag hover
		function FileDragHover(e) {
			e.stopPropagation();
			e.preventDefault();
			e.target.className = (e.type == "dragover" ? "hover" : "");
		}

		
		function FileDragHover(e) {
			e.stopPropagation();
			e.preventDefault();
			e.target.className = (e.type == "dragover" ? "hover" : "");
		}
		
		function FileSelectHandler(e) {
			var files = e.target.files || e.dataTransfer.files;

			for (var i = 0, f; f = files[i]; i++) {
				$('form').ajaxForm({
	        		beforeSend: function() {
	        			$("#popup2").bPopup();
	        			progressBar = new ProgressBar.Line('.bar_percent', {
	       				 strokeWidth: 8,
	       				  easing: 'easeInOut',
	       				  color: '#0091d6',
	       				  trailColor: '#eee',
	       				  trailWidth: 8
	        			});
	        		},
	        		uploadProgress: function(event, position, total, percentComplete) {
	                    var percentVal = percentComplete + '%';
	                    progressBar.width(percentVal);
	                    $("#percent").text(percentVal);
	                },
	                complete: function(xhr) {
	                    progressBar.animate(1
	            				, function() {
	    					$("#popup2").bPopup().close();
	    				});
	                }
	        	});
			}
		}
		$(document).ready(function() {
		$("#uploadFile").on("change", FileSelectHandler);
		$("#fileArea").on("dragover", FileDragHover);
		$("#fileArea").on("dragleave", FileDragHover);
		$('#fileArea').on("drop", FileSelectHandler);
		
		
		});*/
		var voiceId;
		var voiceInfoPath;
		var voiceStreamPath;
		var recogResultPath;
		var progressBar;
		function createVoiceMeta() {
			var url = "/voices/add";
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if(this.readyState == 4 && this.status == 200) {
					var voice = JSON.parse(this.responseText);
					voiceId = voice.voiceId;
					voiceInfoPath = "/voices/" + voiceId;
					voiceStreamPath = voiceInfoPath + "/load";
					recogResultPath = "/recog/" + voiceId;
				}
			};
			xhttp.open("POST", url, true);
			xhttp.setRequestHeader("Content-type", "text/plain");
			xhttp.send();
		}
	
		$('#telList').hide();
		$(function () {
			
			createVoiceMeta();
		     var obj = $("#fileArea");
	
		     obj.on('dragenter', function (e) {
		          e.stopPropagation();
		          e.preventDefault();
		          $(this).css('border', '2px solid #5272A0');
		     });
	
		     obj.on('dragleave', function (e) {
		          e.stopPropagation();
		          e.preventDefault();
		          $(this).css('border', '2px dotted #8296C2');
		     });
	
		     obj.on('dragover', function (e) {
		          e.stopPropagation();
		          e.preventDefault();
		     });
	
		     obj.on('drop', function (e) {
		          e.preventDefault();
		          $(this).css('border', '2px dotted #8296C2');
	
		          var files = e.originalEvent.dataTransfer.files;
		          if(files.length < 1)
		               return;
	
		          F_FileMultiUpload(files);
		     });
		     
		     //var fileField = $("#uploadFile");
		     $("#file-upload").change( function(e) {
		    	 var files = e.target.files;
		    	 if(files.length < 1)
		             return;
		    	 
		    	 F_FileMultiUpload(files);
		     });
		     
		   //파일 멀티 업로드
		     function F_FileMultiUpload(files) {
		    	 var data = new FormData();
		         for (var i = 0; i < files.length; i++) {
		        	 data.append('file', files[i]);
		         }
		         /*
		         $("#popup2").bPopup();
		         progressBar = new ProgressBar.Line('.bar_percent', {
		        	 strokeWidth: 8,
		        	 easing: 'easeInOut',
		        	 color: '#0091d6',
		        	 trailColor: '#eee',
		        	 trailWidth: 8
		         });
		         */
		          var url = '/voices/'+voiceId+'/upload';
		          $.ajax({
						url: url,
						type: 'post',
						data: data,
	//					xhr: function() {
	//		                var myXhr = $.ajaxSettings.xhr();
	//		                if(myXhr.upload){
	//		                    myXhr.upload.addEventListener('progress',progress, false);
	//		                }
	//		                return myXhr;
	//					},
						cache: false,
						contentType: false,
						processData: false
					}).done( function (data) {
						fnUploadSuccess();
					}).fail( function (jqXHR, status, error) {
						alert(jqXHR.responseText + 'Error. Please, contact the webmaster!');
					});
		     }
		     /*
		     function progress(e){
			    if(e.lengthComputable){
			        var max = e.total;
			        var current = e.loaded;
	
			        var Percentage = (current * 100)/max;
			        console.log(Percentage);
			        $("#percent").text(Percentage);
	
			        if(Percentage >= 100) {
			            var intv = setInterval(function () {
			          	  progressBar.set(0);
			          	  progressBar.animate(1);
			            }, 1000);
	//		        	progressBar.animate(1
	//		    				, function() {
	//		    					$("#popup2").bPopup().close();
	//		    				});  
			        }
			    }  
			 }
		     */
		     function progress(e) {
					var progressBar = new ProgressBar.Line('.bar_percent', {
						 strokeWidth: 8,
						  easing: 'easeInOut',
						  duration: 5000,
						  color: '#0091d6',
						  trailColor: '#eee',
						  trailWidth: 8,
						  step: function(state, bar, attachment) {
							  $("#percent").text(Math.ceil(100.0 - state.offset));
						  }
					});
					progressBar.animate(1
					, function() {
						$("#popup2").bPopup().close();
					});				
				}
	
		     function fnUploadSuccess() {
	//	    	 window.location.href = '/player?param=detail&vId='+voiceId;
		    	 $('.file_uploadBox').hide();
		    	 $('.upload .selectWrap').hide();
		    	 $('.recorded').addClass("on");
		    	 //$('#playerDiv2').hide();
	//	    	 $('#realtimeDiv').hide();
	//	    	 $('#resultDiv').show();
		    	 //getVoiceInfo();
		    	 getVoiceStream();
		    	 getRecogResult();
		     }
		});
	

}