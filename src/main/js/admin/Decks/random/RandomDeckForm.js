import React from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {APP_BASE_PATH} from 'admin/AdminApp'

export default function RandomDeckForm() {
  const dispatch = useDispatch()
  const randomColors = useSelector(state => state.decks.random?.colors ?? [])

  const isSelected = (color) => randomColors.includes(color)

  const toggle = (color) => {
    const colors = isSelected(color)
      ? randomColors.filter(it => it !== color)
      : [...randomColors, color]

    dispatch({
      type: 'DECK_RANDOM_COLORS',
      value: colors
    })
  }

  return (
    <>
      <p>Choose which colors you want to play:</p>
      <ul>
        <li>
          <input type="checkbox" id="color-white" name="white" checked={isSelected('WHITE')} onChange={() => toggle('WHITE')}/>
          <label htmlFor="color-white"><img src={APP_BASE_PATH + '/img/symbols/WHITE.png'} alt="white"/>White</label>
        </li>
        <li>
          <input type="checkbox" id="color-blue" name="blue" checked={isSelected('BLUE')} onChange={() => toggle('BLUE')}/>
          <label htmlFor="color-blue"><img src={APP_BASE_PATH + '/img/symbols/BLUE.png'} alt="blue"/>Blue</label>
        </li>
        <li>
          <input type="checkbox" id="color-black" name="black" checked={isSelected('BLACK')} onChange={() => toggle('BLACK')}/>
          <label htmlFor="color-black"><img src={APP_BASE_PATH + '/img/symbols/BLACK.png'} alt="black"/>Black</label>
        </li>
        <li>
          <input type="checkbox" id="color-red" name="red" checked={isSelected('RED')} onChange={() => toggle('RED')}/>
          <label htmlFor="color-red"><img src={APP_BASE_PATH + '/img/symbols/RED.png'} alt="red"/>Red</label>
        </li>
        <li>
          <input type="checkbox" id="color-green" name="green" checked={isSelected('GREEN')} onChange={() => toggle('GREEN')}/>
          <label htmlFor="color-green"><img src={APP_BASE_PATH + '/img/symbols/GREEN.png'} alt="green"/>Green</label>
        </li>
      </ul>
    </>
  )
}
