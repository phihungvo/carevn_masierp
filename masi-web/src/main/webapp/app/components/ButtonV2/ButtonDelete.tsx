import React, { ComponentProps } from 'react'
import ButtonV2 from './ButtonV2'

type Props = Omit<ComponentProps<typeof ButtonV2>, 'children'>

const ButtonDelete = (props: Props) => {
  return (
    <ButtonV2 {...props} variant='text'>
      <img src='content/images/vuesax/linear/trash_v2.svg' alt='delete' />
    </ButtonV2>
  )
}

export default ButtonDelete
