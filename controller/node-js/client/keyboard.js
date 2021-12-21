/*
 * Developed for the OpenBot project (https://openbot.org) by:
 *
 * Ivo Zivkov
 * izivkov@gmail.com
 *
 * Date: Mon Nov 29 2021
 */

export class Keyboard {
  constructor () {
    const menuTable = [
      { keypressCode: 'w', key: 'W', description: 'Go forward' },
      { keypressCode: 's', key: 'S', description: 'Go backward' },
      { keypressCode: 'a', key: 'A', description: 'Turn slightly left (while driving)' },
      { keypressCode: 'd', key: 'D', description: 'Turn slightly right (while driving)' },
      { keypressCode: 'q', key: 'Q', description: 'Rotate left' },
      { keypressCode: 'e', key: 'E', description: 'Rotate right' },
      { keypressCode: 'm', key: 'M', description: 'Drive mode' },
      { keypressCode: 'n', key: 'N', description: 'Toggle noise' },
      { keypressCode: 'ArrowLeft', key: 'Left', description: 'Left indicator' },
      { keypressCode: 'ArrowRight', key: 'Right', description: 'Right indicator' },
      { keypressCode: 'ArrowUp', key: 'Up', description: 'Cancel indicators' },
      { keypressCode: 'ArrowDown', key: 'Down', description: 'Network mode' },
      { keypressCode: ' ', key: 'SPACE', description: 'Toggle logging' },
      { keypressCode: 'Escape', key: 'ESC', description: 'Quit' }
    ]

    // build a set of the keypress codes for a quick lookup
    const keypressSet = new Set(menuTable.map(item => item.keypressCode))

    // currently pressed keys
    const pressedKeys = new Set()

    this.start = (onKeyPress) => {
      const listItems = createMenuList()

      // append all 'li' elements to the root element
      const rootElement = document.getElementById('command-list')
      listItems.forEach(liItem => rootElement.appendChild(liItem))

      processKeys(onKeyPress, listItems)
    }

    // create HTML "li" elements from 'menuTable'
    const createMenuList = () => {
      const listItems = menuTable.map(item => {
        const liItem = document.createElement('li')
        liItem.appendChild(document.createTextNode(`${item.key}: ${item.description}`))
        liItem.setAttribute('key', item.key)
        liItem.setAttribute('keypressCode', item.keypressCode)

        return liItem
      })
      return listItems
    }

    const highlightPressedKeys = list => {
      list.forEach(liItem => {
        const keypressName = liItem.getAttribute('keypressCode')

        liItem.setAttribute('style',
          pressedKeys.has(keypressName) ? 'font-weight: bold' : 'font-weight: normal')
      })
    }

    const isValid = key => keypressSet.has(key)

    const processKeys = (onKeypress, keyList) => {
      document.addEventListener('keydown', (event) => {
        if (!isValid(event.key)) {
          return
        }

        // keep track of pressed key
        pressedKeys.add(event.key)
        highlightPressedKeys(keyList)

        const { key, type } = event
        const keyPerss = { key: key, type: type }

        onKeypress(keyPerss)
      }, false)

      document.addEventListener('keyup', (event) => {
        if (!isValid(event.key)) {
          return
        }

        pressedKeys.delete(event.key)
        highlightPressedKeys(keyList)

        const { key, type } = event
        const keyPerss = { key: key, type: type }

        onKeypress(keyPerss)
      }, false)
    }
  }
}
