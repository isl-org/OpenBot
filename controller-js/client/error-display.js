/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

export class ErrorDisplay {
  constructor () {
    ErrorDisplay.reset()
  }

  static set = (text) => {
    const error = document.getElementById('error-message')
    error.textContent = text
  }

  static reset = () => this.set('')
}
