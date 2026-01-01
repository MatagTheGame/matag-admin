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
        <ColorCheckbox color={'white'} isSelected={isSelected} toggle={toggle} />
        <ColorCheckbox color={'blue'} isSelected={isSelected} toggle={toggle} />
        <ColorCheckbox color={'black'} isSelected={isSelected} toggle={toggle} />
        <ColorCheckbox color={'red'} isSelected={isSelected} toggle={toggle} />
        <ColorCheckbox color={'green'} isSelected={isSelected} toggle={toggle} />
      </ul>
    </>
  )
}

const ColorCheckbox = ({ color, isSelected, toggle}) =>
  <li>
    <input type="checkbox" id={`color-${color}`} name={color} checked={isSelected(color.toUpperCase())} onChange={() => toggle(color.toUpperCase())}/>
    <label htmlFor={`color-${color}`}><img src={`${APP_BASE_PATH}/img/symbols/${color.toUpperCase()}.png`} alt={color}/>{color.charAt(0).toUpperCase() + color.slice(1)}</label>
  </li>
