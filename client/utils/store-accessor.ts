/* eslint-disable import/no-mutable-exports */

import { Store } from "vuex";
import { getModule } from "nuxt-property-decorator";

import General from "~/store/modules/general";

let generalStore: General;

const initializeStores = (store: Store<any>) => {
  generalStore = getModule(General, store);
};

export { initializeStores, generalStore };
