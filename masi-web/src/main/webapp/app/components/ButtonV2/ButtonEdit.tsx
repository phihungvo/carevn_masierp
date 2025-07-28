import { iconPath } from 'app/shared/util/format'
import React, { ComponentProps } from 'react'
import ButtonV2 from './ButtonV2'

type Props = ComponentProps<typeof ButtonV2>

const ButtonEdit = (props: Props) => {
  return (
    <ButtonV2
      {...props}
      variant="text"
      isBoxShadow={false}
    >
      <img src={iconPath('edit-3.svg')} alt="edit" />
    </ButtonV2>
  )
}

export default ButtonEdit
