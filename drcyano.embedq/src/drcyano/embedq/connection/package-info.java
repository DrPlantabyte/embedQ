/**
 * This package holds classes used for bridging the cennection between the Broker and the Client. The
 * reason for this level of re-direction is to allow for remote and intra-process Brokers to have the
 * same public API (it is up to the implemnetations of these interfaces to handle the actual communication
 * between the Client and Broker).
 */
package drcyano.embedq.connection;