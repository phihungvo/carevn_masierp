import { useEffect } from 'react';

export const useFixedColumns = () => {
  useEffect(() => {
    // Get th node list of need to fix right
    const fixedRightThNodeList = document.querySelectorAll('.th-fixed-right') as NodeListOf<HTMLTableColElement>;
    // if no fixed right th node, return
    if (fixedRightThNodeList.length) {
      // Get the last th node offset width to set default right counter
      const lastThNodeOffsetWidth = fixedRightThNodeList[fixedRightThNodeList.length - 1].offsetWidth;
      let counterTh = lastThNodeOffsetWidth - 1.5;
      // Loop through all fixed right th node list to set right position with the formula: right = current node offset width + counter (with counter is the sum of all previous node offset width). The last right node will have right = 0px
      for (let i = fixedRightThNodeList.length - 1; i >= 0; i--) {
        if (i === fixedRightThNodeList.length - 1) {
          fixedRightThNodeList[i].style.right = `0px`;
        } else {
          fixedRightThNodeList[i].style.right = `${counterTh}px`;
          counterTh += fixedRightThNodeList[i].offsetWidth;
        }
      }
    }
    // Do the same thing with td node list
    const fixedRightTdNodeList = document.querySelectorAll('.td-fixed-right') as NodeListOf<HTMLTableColElement>;
    if (fixedRightTdNodeList.length) {
      const lastTdNodeOffsetWidth = fixedRightTdNodeList[fixedRightTdNodeList.length - 1].offsetWidth;
      let counterTd = lastTdNodeOffsetWidth - 1.5;
      for (let i = fixedRightTdNodeList.length - 1; i >= 0; i--) {
        if (i === fixedRightTdNodeList.length - 1) {
          fixedRightTdNodeList[i].style.right = `0px`;
        } else {
          fixedRightTdNodeList[i].style.right = `${counterTd}px`;
          counterTd += fixedRightTdNodeList[i].offsetWidth;
        }
      }
    }
    // Get th node list of need to fix left
    const fixedLeftThNodeList = document.querySelectorAll('.th-fixed-left') as NodeListOf<HTMLTableColElement>;
    // if no fixed left th node, return
    if (fixedLeftThNodeList.length) {
      // Get the first th node offset width to set default right counter
      const firstThNodeOffsetWidth = fixedLeftThNodeList[0].offsetWidth;
      let counterThLeft = firstThNodeOffsetWidth;
      // Loop through all fixed right th node list to set left position with the formula: left = current node offset width + counter (with counter is the sum of all previous node offset width). The first left node will have left = 0px
      for (let i = 0; i < fixedLeftThNodeList.length; i++) {
        if (i === 0) {
          fixedLeftThNodeList[i].style.left = `0px`;
        } else {
          fixedLeftThNodeList[i].style.left = `${counterThLeft}px`;
          counterThLeft += fixedLeftThNodeList[i].offsetWidth;
        }
      }
    }
    // Get th node list of need to fix left
    const fixedLeftTdNodeList = document.querySelectorAll('.td-fixed-left') as NodeListOf<HTMLTableColElement>;
    if (fixedLeftTdNodeList.length) {
      const firstTdNodeOffsetWidth = fixedLeftTdNodeList[0].offsetWidth;
      let counterTdLeft = firstTdNodeOffsetWidth;
      for (let i = 0; i < fixedLeftTdNodeList.length; i++) {
        if (i === 0) {
          fixedLeftTdNodeList[i].style.left = `0px`;
        } else {
          fixedLeftTdNodeList[i].style.left = `${counterTdLeft}px`;
          counterTdLeft += fixedLeftTdNodeList[i].offsetWidth;
        }
      }
    }
  }, []);
};
