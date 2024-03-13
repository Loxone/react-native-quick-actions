module.exports = {
  /**
   * An initial action will be available if the app was cold-launched
   * from an action.
   *
   * The first caller of `popInitialAction` will get the initial
   * action object, or `null`. Subsequent invocations will return null.
   */
  popInitialAction: function() {

  },

  /**
   * Adds shortcut items to application
   */
  setShortcutItems: function(items) {

  },

  /**
   * Clears all previously set dynamic icons
   */
  clearShortcutItems: function() {

  },

  /**
   * Check if quick actions are supported
   */
   isSupported: function(callback) {

   }
};
