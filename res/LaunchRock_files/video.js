
window.lrignition.themesJS.video = function() {
	
	var self = this;
	//self.funcs.ignoreMainBGImage = true;
	
	var updateEmbedCode = function(ignition, name, value) {
		var $el = ignition.getContainer().find('.LR-video-container');
		if(value && value != 'none' && value != 'null') {
			$el.html(value);
		} else {
			$el.html('<img src="js/ignition/themes/video/img/videoplayer.png" alt="img"/>');
		}
	};
	
	$.extend(this.themeSpec, {
		"themesettings.videoEmbedCode": updateEmbedCode,
		"taglineToggle": [ "show .LR-site-tagline", "invCondClass .LR-box LR-noTagline" ]
	});
	
	/*this.funcs.updateHeaderBG = function(ignition, name, value) {
		self.updateGenericBG(ignition, ignition.getContainer().find('.LR-header'), ignition.getContainer().find('.LR-bg-img'), "themesettings.headerBgColorHex", "themesettings.headerAlpha", "themesettings.headerBgImage", "themesettings.headerBgImageToggle", true);
	};*/
	
	this.init.push(function() {
	});
	
	// Uncomment to override default mode set behavior
	/*this.setMode = function(ignition, mode) {
		var container = ignition.getContainer();
		if(mode == "main") {
			container.find(".LR-content").removeClass("LR-sharing-page");
			container.find(".LR-site-share").hide();
			container.find(".LR-sign-up-container").show();
		} else if(mode == "postsignup") {
			container.find(".LR-content").addClass("LR-sharing-page");
			container.find(".LR-sign-up-container").hide();
			container.find(".LR-site-share").show();
		}
	};*/
	
};

window.lrignition.themesJS.video.prototype = new window.lrignition.themesJS.common("video");
