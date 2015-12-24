
if(!window.lrignition.themesJS) window.lrignition.themesJS = {};

window.lrignition.themesJS["common"] = function(themeName, options) {
	
	this.themeName = themeName;
	var self = this;
	
	this.funcs = {};
	
	var getBooleanSetting = this.getBooleanSetting = function(ignition, name) {
		return lrSiteSettingAsBoolean(ignition.getSetting(name));
	};
	
	var updateBoxBG = this.updateBoxBG = function(ignition, colorVal, alphaVal) {
		var rgb = window.lrignition.ThemeBase.hexColorToRGB(colorVal);
		$.each( ['.LR-box', '.LR-announcement-bar'], function(selIdx, selector) {
			ignition.getContainer().find(selector).css(
				'background',
				'rgb('
				+ rgb.r + "," + rgb.g + "," + rgb.b
				+ ')'
			);
			ignition.getContainer().find(selector).css(
				'background',
				'rgba('
				+ rgb.r + "," + rgb.g + "," + rgb.b + ","
				+ (alphaVal / 100)
				+ ')'
			);
		} );
	};
	
	var updateExtraFieldsClass = function(ignition, name, value) {
		var fields = [ 'full-name', 'phone', 'company', 'zip', 'dob' ];
		var hasExtraFields = false;
		$.each(fields, function(idx, field) {
			if(getBooleanSetting(ignition, "extraFields.extra-field-" + field)) hasExtraFields = true;
		});
		if(hasExtraFields) ignition.getContainer().find('.LR-sign-up-container').addClass('LR-extra-fields');
		else ignition.getContainer().find('.LR-sign-up-container').removeClass('LR-extra-fields');
	};
	
	/*var updateBG = this.updateBG = function(ignition, name, value) {
		if(!self.funcs.ignoreMainBGImage) ignition.getContainer().find('.LR-bg-img').show();
		if(getBooleanSetting(ignition, "bgToggle") && ignition.getSetting("bgImage") && !self.funcs.ignoreMainBGImage) {
			$('body').css('background', '');
			ignition.getContainer().find(".LR-bg-img").css("background-image", "url('" + ignition.getSetting("bgImage") + "')");
		} else {
			if(!self.funcs.ignoreMainBGImage) ignition.getContainer().find(".LR-bg-img").css("background-image", "");
			var bgColorEl = $("#themeLandingPage");
			if(bgColorEl.length == 0) bgColorEl = $("body");
			if(ignition.getSetting("themesettings.bgColorHex")) {
				if(!getBooleanSetting(ignition, "bgToggle") && !self.funcs.ignoreMainBGImage) ignition.getContainer().find('.LR-bg-img').hide();
				var rgb = window.lrignition.ThemeBase.hexColorToRGB(ignition.getSetting("themesettings.bgColorHex"));
				bgColorEl.css(
					'background',
					'rgb(' + rgb.r + "," + rgb.g + "," + rgb.b + ')'
				);
			} else {
				bgColorEl.css('background', '');
			}
		}
	};*/
	
	var updateLinkExBG = function(ignition, name, value) {
		if(ignition.getSetting("themesettings.linkExBgColorHex")) {
			var rgb = window.lrignition.ThemeBase.hexColorToRGB(ignition.getSetting("themesettings.bgColorHex"));
			ignition.getContainer().find('#LR-exchangeContainer').css(
				'background',
				'rgb('
				+ rgb.r + "," + rgb.g + "," + rgb.b
				+ ')'
			);
		} else {
			ignition.getContainer().find('#LR-exchangeContainer').css('background', '');
		}
	};
	
	var getStandardBGSettings = function(type) {
		var hexColorSetting = null, alphaSetting = null, imageSetting = null, imageToggleSetting = null;
		switch(type) {
			case 'site':
				hexColorSetting = 'themesettings.bgColorHex';
				imageSetting = 'bgImage';
				imageToggleSetting = 'bgToggle';
				break;
			case 'header':
				hexColorSetting = 'themesettings.headerBgColorHex';
				alphaSetting = 'themesettings.headerAlpha';
				imageSetting = 'themesettings.headerBgImage';
				imageToggleSetting = 'themesettings.headerBgImageToggle';
				break;
			case 'announcement':
				hexColorSetting = 'themesettings.announcementBgColorHex';
				alphaSetting = 'themesettings.announcementAlpha';
				break;
			default:
				hexColorSetting = 'themesettings.' + type + 'BgColorHex';
				alphaSetting = 'themesettings.' + type + 'Alpha';
				imageSetting = 'themesettings.' + type + 'BgImage';
				imageToggleSetting = 'themesettings.' + type + 'BgImageToggle';
				break;
		}
		return { hexColorSetting : hexColorSetting, alphaSetting : alphaSetting, imageSetting : imageSetting, imageToggleSetting : imageToggleSetting };
	};
	
	var getStandardBGValues = function(ignition, type) {
		var settings = getStandardBGSettings(type);
		return {
			hexColor : settings.hexColorSetting ? ignition.getSetting(settings.hexColorSetting) : null,
			alpha : settings.alphaSetting ? ignition.getSetting(settings.alphaSetting) : null,
			image : settings.imageSetting ? ignition.getSetting(settings.imageSetting) : null,
			imageToggle : settings.imageToggleSetting ? getBooleanSetting(ignition, settings.imageToggleSetting) : true
		};
	};
	
	var updateStandardBG = function(ignition, type) {
		var values = getStandardBGValues(ignition, type);
		var container = ignition.getContainer();
		if(values.image && values.imageToggle) {
			container.find('.LR-' + type + '-bg-image-container').css('background-image', "url('" + values.image + "')");
			container.find('.LR-' + type + '-show-on-bg-image').show();
		} else {
			container.find('.LR-' + type + '-show-on-bg-image').hide();
		}
		if(values.hexColor) {
			var rgb = window.lrignition.ThemeBase.hexColorToRGB(values.hexColor);
			$colorEl = container.find('.LR-' + type + '-bg-color-container');
			$colorEl.css(
				'background',
				'rgb('
				+ rgb.r + "," + rgb.g + "," + rgb.b
				+ ')'
			);
			if(values.alpha !== null) {
				$colorEl.css(
					'background',
					'rgba('
					+ rgb.r + "," + rgb.g + "," + rgb.b + ","
					+ (values.alpha / 100)
					+ ')'
				);
			}
			container.find('.LR-' + type + '-show-on-bg-color').show();
		} else {
			container.find('.LR-' + type + '-show-on-bg-color').hide();
		}
	};
	
	var registerStandardBG = this.registerStandardBG = function(type) {
		var updateFunc = function(ignition, name, value) {
			updateStandardBG(ignition, type);
		};
		var settings = getStandardBGSettings(type);
		$.each(settings, function(settingType, settingName) {
			if(settingName) self.themeSpec[settingName] = updateFunc;
		});
	};
	
/*	var updateGenericBG = this.updateGenericBG = function(ignition, $colorel, $imageel, hexColorSetting, alphaSetting, imageSetting, imageToggleSetting, showHideImage) {
		if(imageSetting && ignition.getSetting(imageSetting) && (!imageToggleSetting || getBooleanSetting(ignition, imageToggleSetting))) {
			if($colorel) $colorel.css('background', '');
			if($imageel) $imageel.css('background-image', "url('" + ignition.getSetting(imageSetting) + "')");
			if(showHideImage) $imageel.show();
		} else if(hexColorSetting && ignition.getSetting(hexColorSetting)) {
			if($imageel) $imageel.css('background-image', '');
			if(showHideImage) $imageel.hide();
			var rgb = window.lrignition.ThemeBase.hexColorToRGB(ignition.getSetting(hexColorSetting));
			if($colorel) $colorel.css(
				'background',
				'rgb('
				+ rgb.r + "," + rgb.g + "," + rgb.b
				+ ')'
			);
			if(alphaSetting && ignition.getSetting(alphaSetting)) {
				var alphaVal = ignition.getSetting(alphaSetting);
				if($colorel) $colorel.css(
					'background',
					'rgba('
					+ rgb.r + "," + rgb.g + "," + rgb.b + ","
					+ (alphaVal / 100)
					+ ')'
				);
			}
		} else {
			if($colorel) $colorel.css('background', '');
			if($imageel) $imageel.css('background-image', '');
			if(showHideImage) $imageel.hide();
		}
	};
	
	var updateGenericBGFunc = this.updateGenericBGFunc = function(colorselector, imageselector, hexColorSetting, alphaSetting, imageSetting) {
		return function(ignition, name, value) {
			updateGenericBG(ignition, ignition.getContainer().find(colorselector), ignition.getContainer().find(imageselector), hexColorSetting, alphaSetting, imageSetting);
		};
	};
	
	var updateAnnouncementBG = this.updateAnnouncementBG = function(ignition, name, value) {
		updateGenericBG(ignition, ignition.getContainer().find('.LR-announcement-bar'), ignition.getContainer().find('.LR-announcement-bar'), "themesettings.announcementBgColorHex", "themesettings.announcementAlpha", "themesettings.announcementBgImage");
	};
	
	this.funcs.updateHeaderBG = function(ignition, name, value) {
		self.updateGenericBG(ignition, ignition.getContainer().find('.LR-header'), ignition.getContainer().find('.LR-bg-img'), "themesettings.headerBgColorHex", "themesettings.headerAlpha", "themesettings.headerBgImage", "themesettings.headerBgImageToggle", true);
	};*/
	
	var checkTitleVisibility = function(ignition) {
		if(getBooleanSetting(ignition, "logoToggle") && ignition.getSetting("logo")) {
			if(getBooleanSetting(ignition, "logoToggle")) ignition.getContainer().find(".LR-site-logo").show();
			ignition.getContainer().find(".LR-site-title").hide();
		} else {
			ignition.getContainer().find(".LR-site-logo").hide();
			ignition.getContainer().find(".LR-site-title").show();
		}
	};
	
	var updateLinkEx = function(ignition, settingName, siteLinks) {
		var container = ignition.getContainer();
		if(siteLinks.length == 0) {
			container.find('.LR-content').removeClass('LR-lx');
		} else {
			
			var updateLinkExSite = function(el, settings, url) {
				if(settings.linkExLogo) el.find('.LR-lxImg img').attr('src', settings.linkExLogo);
				else el.find('.LR-lxImg img').hide();
				if(settings.linkExSiteTagline) el.find('.LR-lxTxt').text(settings.linkExSiteTagline);
				else el.find('.LR-lxTxt').text("");
				el.unbind('click').click(function() {
					document.location.href = url;
				});
			};
			
			updateLinkExSite(container.find('.LR-lxCompanyOne'), siteLinks[0].settings, siteLinks[0].url);
			container.find('.LR-lxCompanyOne').show();
			
			if(siteLinks.length > 1) {
				updateLinkExSite(container.find('.LR-lxCompanyTwo'), siteLinks[1].settings, siteLinks[1].url);
				container.find('.LR-lxCompanyTwo').show();
			} else {
				container.find('.LR-lxCompanyTwo').hide();
			}
			
			container.find('.LR-content').addClass('LR-lx');
		}
	};
	
	var checkShowFindUsOn = function(ignition) {
		var toggles = [
			"socialLinks.twitter-social-link.toggled",
			"socialLinks.facebook-social-link.toggled",
			"socialLinks.rss-social-link.toggled",
			"socialLinks.tumblr-social-link.toggled",
			"socialLinks.pinterest-social-link.toggled",
			"socialLinks.instagram-social-link.toggled",
			"socialLinks.github-social-link.toggled",
			"socialLinks.youtube-social-link.toggled",
			"socialLinks.vimeo-social-link.toggled",
			"socialLinks.blog-social-link.toggled"
		];
		var showIt = false;
		$.each(toggles, function(idx, toggle) {
			if(getBooleanSetting(ignition, toggle)) showIt = true;
		});
		var el = ignition.getContainer().find('.LR-site-connect .LR-share-label');
		if(showIt) el.css('visibility', ''); else el.css('visibility', 'hidden');
	};
	
	this.themeSpec = $.extend({}, window.lrignition.ThemeBase.themeSpec, {
		siteName: "text .LR-site-title SETTINGVALUE",
		announcementBanner: "html .LR-announcement SETTINGVALUE",
		announcementBannerToggle: "show .LR-announcement-bar",
		logo: [ "html .LR-site-logo <img src=\"SETTINGVALUE\" />", checkTitleVisibility ],
		logoToggle: checkTitleVisibility,
		siteNameToggle: checkTitleVisibility,
		incentive: "html .LR-site-incentive SETTINGVALUE",
		incentiveToggle: "show .LR-site-incentive",
		tagline: "html .LR-site-tagline SETTINGVALUE",
		taglineToggle: "show .LR-site-tagline",
		description: "html .LR-site-description SETTINGVALUE",
		descriptionToggle: "show .LR-site-description",
		inviteList: "html .LR-sign-up-label SETTINGVALUE",
		instructionsToggle: "show .LR-sign-up-label",
		emailShareToggle: "show .LR-site-share-email",
		fbRecommendToggle: "show .LR-share-facebook-like",
		fbShareToggle: "show .LR-share-facebook-send",
		tweetToggle: "show .LR-share-tweet",
		linkedInShareToggle: "show .LR-share-linkedin",
		shareOnTumblrToggle: "show .LR-share-tumblr",
//		bgImage: updateBG,
//		bgToggle: updateBG,
//		"themesettings.bgColorHex": updateBG,
		"themesettings.linkExBgColorHex": updateLinkExBG,
//		"themesettings.announcementBgColorHex": updateAnnouncementBG,
//		"themesettings.announcementAlpha": updateAnnouncementBG,
//		"themesettings.announcementImage": updateAnnouncementBG,
//		"themesettings.headerBgColorHex": function(a, b, c) { self.funcs.updateHeaderBG(a, b, c); },
//		"themesettings.headerAlpha": function(a, b, c) { self.funcs.updateHeaderBG(a, b, c); },
//		"themesettings.headerBgImage": function(a, b, c) { self.funcs.updateHeaderBG(a, b, c); },
//		"themesettings.headerBgImageToggle": function(a, b, c) { self.funcs.updateHeaderBG(a, b, c); },
		linkExLinks: updateLinkEx,
		"extraFields.extra-field-full-name": [ "show .first-name", "show .last-name", updateExtraFieldsClass ],
		"extraFields.extra-field-phone": [ "show .phone-number", updateExtraFieldsClass ],
		"extraFields.extra-field-company": [ "show .company", updateExtraFieldsClass ],
		"extraFields.extra-field-zip": [ "show .zipcode", updateExtraFieldsClass ],
		"extraFields.extra-field-dob": [ "show .birthdate", updateExtraFieldsClass ],
		"socialLinks.twitter-social-link.toggled": ["show .connect-twitter", checkShowFindUsOn],
		"socialLinks.twitter-social-link.value": "clickLink .connect-twitter SETTINGVALUE",
		"socialLinks.facebook-social-link.toggled": ["show .connect-facebook", checkShowFindUsOn],
		"socialLinks.facebook-social-link.value": "clickLink .connect-facebook SETTINGVALUE",
		"socialLinks.rss-social-link.toggled": ["show .connect-rss", checkShowFindUsOn],
		"socialLinks.rss-social-link.value": "clickLink .connect-rss SETTINGVALUE",
		"socialLinks.tumblr-social-link.toggled": ["show .connect-tumblr", checkShowFindUsOn],
		"socialLinks.tumblr-social-link.value": "clickLink .connect-tumblr SETTINGVALUE",
		"socialLinks.pinterest-social-link.toggled": ["show .connect-pinterest", checkShowFindUsOn],
		"socialLinks.pinterest-social-link.value": "clickLink .connect-pinterest SETTINGVALUE",
		"socialLinks.instagram-social-link.toggled": ["show .connect-instagram", checkShowFindUsOn],
		"socialLinks.instagram-social-link.value": "clickLink .connect-instagram SETTINGVALUE",
		"socialLinks.github-social-link.toggled": ["show .connect-github", checkShowFindUsOn],
		"socialLinks.github-social-link.value": "clickLink .connect-github SETTINGVALUE",
		"socialLinks.youtube-social-link.toggled": ["show .connect-youtube", checkShowFindUsOn],
		"socialLinks.youtube-social-link.value": "clickLink .connect-youtube SETTINGVALUE",
		"socialLinks.vimeo-social-link.toggled": ["show .connect-vimeo", checkShowFindUsOn],
		"socialLinks.vimeo-social-link.value": "clickLink .connect-vimeo SETTINGVALUE",
		"socialLinks.blog-social-link.toggled": ["show .connect-blog", checkShowFindUsOn],
		"socialLinks.blog-social-link.value": "clickLink .connect-blog SETTINGVALUE",
		favicon: function(ignition, name, value) {
			if(!ignition.isWidget() && ignition.isLive()) {
				$('head').append(
					$('<link />').attr('rel', 'shortcut icon').attr('href', value)
				);
			}
		}
	});
	
	registerStandardBG('site');
	registerStandardBG('header');
	registerStandardBG('announcement');
	
	this.setMode = function(ignition, mode) {
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
	};
	
	this.init = [];
	
	this.init.push(function(ignition) {
		var container = ignition.getContainer();
		// Only show additional share by email fields when there's at least one character typed in the box
		var emailListEl = container.find('.LR-share-email-emails');
		var emailOtherFieldsEl = container.find('.LR-share-email-hide');
		emailOtherFieldsEl.hide();
		emailListEl.unbind('input').bind('input', function() {
			if(emailListEl.val()) emailOtherFieldsEl.show();
			else emailOtherFieldsEl.hide();
		});
	});
	
};

window.lrignition.themesJS["common"].prototype = window.lrignition.ThemeBase;
