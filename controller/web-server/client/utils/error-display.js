/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

export function ErrorDisplay () {
  this.set = (text) => {
    const error = document.getElementById('error-message')
    error.textContent = text
  }

  this.reset = () => this.set('')

  this.reset()
}
