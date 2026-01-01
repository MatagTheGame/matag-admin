import React from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {APP_BASE_PATH} from 'admin/AdminApp'

export default function RandomDeckForm() {
  const dispatch = useDispatch()
  const dispatchDeckRandomEvent = (value) => dispatch({type: 'DECK_RANDOM', value})
  const randomDeck = useSelector(state => state.decks.random ?? {colors: [], sets: ['ALL']})

  const isColorSelected = (color) => randomDeck.colors.includes(color)
  const isAllSetsSelected = () => randomDeck.sets.includes('ALL')
  const isSomeSetsSelected = () => !isAllSetsSelected()

  const toggleColor = (color) => {
    const colors = isColorSelected(color) ? randomDeck.colors.filter(it => it !== color) : [...randomDeck.colors, color]
    dispatchDeckRandomEvent({...randomDeck, colors})
  }

  const selectAllSets = () => dispatchDeckRandomEvent({ ...randomDeck, sets: ['ALL'] })
  const selectSomeSets = () =>  dispatchDeckRandomEvent({ ...randomDeck, sets: [] })

  return (
    <>
      <p>What colors?</p>
      <ul>
        <ColorCheckbox color={'White'} isSelected={isColorSelected} toggle={toggleColor} />
        <ColorCheckbox color={'Blue'} isSelected={isColorSelected} toggle={toggleColor} />
        <ColorCheckbox color={'Black'} isSelected={isColorSelected} toggle={toggleColor} />
        <ColorCheckbox color={'Red'} isSelected={isColorSelected} toggle={toggleColor} />
        <ColorCheckbox color={'Green'} isSelected={isColorSelected} toggle={toggleColor} />
      </ul>

      <p>What set?</p>
      <ul>
        <SetRadio type={'All'} isSelected={isAllSetsSelected} toggle={selectAllSets} />
        <SetRadio type={'Some'} isSelected={isSomeSetsSelected} toggle={selectSomeSets} />
        { isSomeSetsSelected() && <SetDropDown selectedSets={randomDeck.sets} allSets={[]} toggle={() => {}} /> }
      </ul>
    </>
  )
}

const ColorCheckbox = ({ color, isSelected, toggle: toggle}) =>
  <li>
    <input type="checkbox" id={`color-${color}`} name={color} checked={isSelected(color.toUpperCase())} onChange={() => toggle(color.toUpperCase())}/>
    <label htmlFor={`color-${color}`}><img src={`${APP_BASE_PATH}/img/symbols/${color.toUpperCase()}.png`} alt={color}/>{color}</label>
  </li>

const SetRadio = ({ type, isSelected, toggle: toggle}) =>
  <li>
    <input id={`sets-${type}`} type="radio" value="SETS" name="sets" checked={isSelected()} onChange={() => toggle()}/>
    <label htmlFor={`sets-${type}`}>{type}</label>
  </li>

const SetDropDown = ({selectedSets, allSets, toggle}) =>
  <li>
    <select multiple={true} value={selectedSets} onChange={() => toggle()}>
      <option value="RED">Red</option>
      <option value="BLUE">Blue</option>
      <option value="GREEN">Green</option>
    </select>
  </li>
