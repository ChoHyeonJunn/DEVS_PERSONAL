/**
 * @license Copyright (c) 2003-2019, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	config.toolbar =
		[
		       
		       { name: 'clipboard',   items : [ 'Undo','Redo' ] },
		       { name: 'editing',     items : [ 'Find','Replace','-','SelectAll' ] },
		       { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike' ] },
		       { name: 'paragraph',   items : [ 'JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock' ] },
		       '/',
		       { name: 'styles',      items : [ 'Font','FontSize' ] },
		       { name: 'colors',      items : [ 'TextColor','BGColor' ] },
		       { name: 'links',       items : [ 'Link','Unlink' ] },
		       { name: 'insert',      items : [ 'Image'] }
		];
	config.filebrowserUploadMethod = 'form';
	
};
